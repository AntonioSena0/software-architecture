const productList = document.getElementById('product-list');
const productForm = document.getElementById('product-form');
const statusSpan = document.getElementById('status');
let eventSource = null;

function renderProduct(product, isNew = false) {
    const li = document.createElement('li');
    li.className = 'product-item';
    li.dataset.id = product.id;

    const info = document.createElement('div');
    info.className = 'product-info';

    const name = document.createElement('span');
    name.className = 'product-name';
    name.textContent = product.name;

    const price = document.createElement('span');
    price.className = 'product-price';
    price.textContent = `R$ ${Number(product.price).toFixed(2)}`;

    const desc = document.createElement('p');
    desc.className = 'product-description';
    desc.textContent = product.description || '';

    const btn = document.createElement('button');
    btn.className = 'btn btn-danger';
    btn.textContent = 'Excluir';
    btn.addEventListener('click', () => deleteProduct(product.id));

    info.append(name, price, desc);
    li.append(info, btn);
    productList.appendChild(li);

    if (isNew) {
        li.classList.add('new-item');
    }
}

async function loadInitialProducts() {
    try {
        const response = await fetch('/api/v1/products');
        const products = await response.json();
        productList.innerHTML = '';

        const content = products.content ? products.content : products;
        content.forEach(product => renderProduct(product, false));
    } catch (error) {
        console.error('Erro ao carregar lista inicial:', error);
    }
}

async function createProduct(event) {
    event.preventDefault();
    const name = document.getElementById('name').value;
    const price = parseFloat(document.getElementById('price').value);
    const stock = parseInt(document.getElementById('stock').value, 10);
    const description = document.getElementById('description').value.trim() || null;

    try {
        const response = await fetch('/api/v1/products', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, price, stock, description })
        });

        if (!response.ok) {
            const errorData = await response.json();
            return;
        }

        productForm.reset();
    } catch (error) {
        console.error('Erro ao criar produto:', error);
    }
}

async function deleteProduct(id) {
    try {
        await fetch(`/api/v1/products/${id}`, { method: 'DELETE' });
    } catch (error) {
        console.error('Erro ao deletar produto:', error);
    }
}

function startSseStream() {

    if (eventSource) {
        eventSource.close();
    }

    let retries = 0;
    const MAX_RETRIES = 5;

    eventSource = new EventSource('/api/v1/products/stream');

    eventSource.onopen = () => {
        statusSpan.textContent = 'Conectado (Live)';
        statusSpan.className = 'status online';

        retries = 0
    };

    defineEvents();

    eventSource.onerror = () => {

        if(retries < MAX_RETRIES) {
            const delay = Math.min(1000 * 2 ** retries, 30000);
            retries++

            eventSource.close();

            setTimeout(() => {
                eventSource = new EventSource('/api/v1/products/stream');
                defineEvents();
            }, delay);
        }

        statusSpan.textContent = 'Reconectando...';
        statusSpan.className = 'status offline';
    };
}

function defineEvents() {

    eventSource.addEventListener('product-created', (event) => {
        const newProduct = JSON.parse(event.data);
        renderProduct(newProduct, true);
    });

    eventSource.addEventListener('product-deleted', (event) => {
        const deletedId = JSON.parse(event.data);
        const element = document.querySelector(`[data-id="${deletedId}"]`);
        if (element) {
            element.remove();
        }
    });

}

window.addEventListener('beforeunload', () => {
    if (eventSource) {
        eventSource.close();
    }
});

productForm.addEventListener('submit', createProduct);
loadInitialProducts();
startSseStream();