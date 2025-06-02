// Configuração da API
const API_URL = 'http://localhost:8080';

// Funções utilitárias
function formatarData(data) {
    if (!data) return '-';
    const date = new Date(data);
    return date.toLocaleDateString('pt-BR');
}

function formatarValor(valor) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
}

// Funções para Categorias
async function carregarCategorias() {
    try {
        const response = await fetch(`${API_URL}/categorias`);
        const categorias = await response.json();

        // Limpa a tabela
        const tbody = document.getElementById('categorias-table-body');
        tbody.innerHTML = '';

        // Preenche o select de categorias no formulário de lançamentos
        const selectCategoria = document.getElementById('categoria');
        selectCategoria.innerHTML = '<option value="">Selecione uma categoria</option>';

        // Adiciona as categorias na tabela e no select
        categorias.forEach(categoria => {
            // Adiciona na tabela
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${categoria.nome}</td>
                <td class="acoes">
                    <button class="btn-editar" data-id="${categoria.codigo}">Editar</button>
                    <button class="btn-excluir" data-id="${categoria.codigo}">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);

            // Adiciona no select
            const option = document.createElement('option');
            option.value = categoria.codigo;
            option.textContent = categoria.nome;
            selectCategoria.appendChild(option);

            // Adiciona eventos aos botões
            tr.querySelector('.btn-editar').addEventListener('click', () => editarCategoria(categoria.codigo));
            tr.querySelector('.btn-excluir').addEventListener('click', () => excluirCategoria(categoria.codigo));
        });
    } catch (error) {
        console.error('Erro ao carregar categorias:', error);
        alert('Erro ao carregar categorias. Verifique se a API está rodando.');
    }
}

async function salvarCategoria(event) {
    event.preventDefault();

    const nome = document.getElementById('nome-categoria').value;
    const categoriaId = document.getElementById('categoria-form').dataset.id;

    const categoria = { nome };

    try {
        let url = `${API_URL}/categorias`;
        let method = 'POST';

        // Se tiver ID, é uma atualização
        if (categoriaId) {
            url = `${url}/${categoriaId}`;
            method = 'PUT';
            categoria.codigo = categoriaId;
        }

        const response = await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(categoria)
        });

        if (response.ok) {
            document.getElementById('categoria-form').reset();
            document.getElementById('form-categoria').style.display = 'none';
            document.getElementById('categoria-form').dataset.id = '';
            carregarCategorias();
        } else {
            const error = await response.json();
            alert(`Erro ao salvar categoria: ${error.message || 'Erro desconhecido'}`);
        }
    } catch (error) {
        console.error('Erro ao salvar categoria:', error);
        alert('Erro ao salvar categoria. Verifique se a API está rodando.');
    }
}

async function editarCategoria(id) {
    try {
        const response = await fetch(`${API_URL}/categorias/${id}`);
        const categoria = await response.json();

        document.getElementById('nome-categoria').value = categoria.nome;
        document.getElementById('categoria-form').dataset.id = categoria.codigo;
        document.getElementById('form-categoria').style.display = 'block';
    } catch (error) {
        console.error('Erro ao carregar categoria para edição:', error);
        alert('Erro ao carregar categoria para edição.');
    }
}

async function excluirCategoria(id) {
    if (!confirm('Tem certeza que deseja excluir esta categoria?')) return;

    try {
        const response = await fetch(`${API_URL}/categorias/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            carregarCategorias();
        } else {
            alert('Erro ao excluir categoria.');
        }
    } catch (error) {
        console.error('Erro ao excluir categoria:', error);
        alert('Erro ao excluir categoria.');
    }
}

// Funções para Pessoas
async function carregarPessoas() {
    try {
        const response = await fetch(`${API_URL}/pessoas`);
        const pessoas = await response.json();

        // Limpa a tabela
        const tbody = document.getElementById('pessoas-table-body');
        tbody.innerHTML = '';

        // Preenche o select de pessoas no formulário de lançamentos
        const selectPessoa = document.getElementById('pessoa');
        selectPessoa.innerHTML = '<option value="">Selecione uma pessoa</option>';

        // Adiciona as pessoas na tabela e no select
        pessoas.forEach(pessoa => {
            // Adiciona na tabela
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${pessoa.nome}</td>
                <td>${pessoa.endereco ? pessoa.endereco.cidade : '-'}</td>
                <td>${pessoa.endereco ? pessoa.endereco.estado : '-'}</td>
                <td class="status ${pessoa.ativo ? 'ativo' : 'inativo'}">${pessoa.ativo ? 'Ativo' : 'Inativo'}</td>
                <td class="acoes">
                    <button class="btn-editar" data-id="${pessoa.codigo}">Editar</button>
                    <button class="btn-excluir" data-id="${pessoa.codigo}">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);

            // Adiciona no select (apenas pessoas ativas)
            if (pessoa.ativo) {
                const option = document.createElement('option');
                option.value = pessoa.codigo;
                option.textContent = pessoa.nome;
                selectPessoa.appendChild(option);
            }

            // Adiciona eventos aos botões
            tr.querySelector('.btn-editar').addEventListener('click', () => editarPessoa(pessoa.codigo));
            tr.querySelector('.btn-excluir').addEventListener('click', () => excluirPessoa(pessoa.codigo));
        });
    } catch (error) {
        console.error('Erro ao carregar pessoas:', error);
        alert('Erro ao carregar pessoas. Verifique se a API está rodando.');
    }
}

async function salvarPessoa(event) {
    event.preventDefault();

    const nome = document.getElementById('nome').value;
    const ativo = document.getElementById('ativo').checked;
    const logradouro = document.getElementById('logradouro').value;
    const numero = document.getElementById('numero').value;
    const complemento = document.getElementById('complemento').value;
    const bairro = document.getElementById('bairro').value;
    const cep = document.getElementById('cep').value;
    const cidade = document.getElementById('cidade').value;
    const estado = document.getElementById('estado').value;

    const pessoaId = document.getElementById('pessoa-form').dataset.id;

    const pessoa = {
        nome,
        ativo,
        endereco: {
            logradouro,
            numero,
            complemento,
            bairro,
            cep,
            cidade,
            estado
        }
    };

    try {
        let url = `${API_URL}/pessoas`;
        let method = 'POST';

        // Se tiver ID, é uma atualização
        if (pessoaId) {
            url = `${url}/${pessoaId}`;
            method = 'PUT';
            pessoa.codigo = pessoaId;
        }

        const response = await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(pessoa)
        });

        if (response.ok) {
            document.getElementById('pessoa-form').reset();
            document.getElementById('form-pessoa').style.display = 'none';
            document.getElementById('pessoa-form').dataset.id = '';
            carregarPessoas();
        } else {
            const error = await response.json();
            alert(`Erro ao salvar pessoa: ${error.message || 'Erro desconhecido'}`);
        }
    } catch (error) {
        console.error('Erro ao salvar pessoa:', error);
        alert('Erro ao salvar pessoa. Verifique se a API está rodando.');
    }
}

async function editarPessoa(id) {
    try {
        const response = await fetch(`${API_URL}/pessoas/${id}`);
        const pessoa = await response.json();

        document.getElementById('nome').value = pessoa.nome;
        document.getElementById('ativo').checked = pessoa.ativo;

        if (pessoa.endereco) {
            document.getElementById('logradouro').value = pessoa.endereco.logradouro || '';
            document.getElementById('numero').value = pessoa.endereco.numero || '';
            document.getElementById('complemento').value = pessoa.endereco.complemento || '';
            document.getElementById('bairro').value = pessoa.endereco.bairro || '';
            document.getElementById('cep').value = pessoa.endereco.cep || '';
            document.getElementById('cidade').value = pessoa.endereco.cidade || '';
            document.getElementById('estado').value = pessoa.endereco.estado || '';
        }

        document.getElementById('pessoa-form').dataset.id = pessoa.codigo;
        document.getElementById('form-pessoa').style.display = 'block';
    } catch (error) {
        console.error('Erro ao carregar pessoa para edição:', error);
        alert('Erro ao carregar pessoa para edição.');
    }
}

async function excluirPessoa(id) {
    if (!confirm('Tem certeza que deseja desativar esta pessoa?')) return;

    try {
        const response = await fetch(`${API_URL}/pessoas/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            carregarPessoas();
        } else {
            alert('Erro ao desativar pessoa.');
        }
    } catch (error) {
        console.error('Erro ao desativar pessoa:', error);
        alert('Erro ao desativar pessoa.');
    }
}

// Funções para Lançamentos
async function carregarLancamentos() {
    try {
        const response = await fetch(`${API_URL}/lancamentos`);
        const lancamentos = await response.json();

        // Limpa a tabela
        const tbody = document.getElementById('lancamentos-table-body');
        tbody.innerHTML = '';

        // Adiciona os lançamentos na tabela
        lancamentos.forEach(lancamento => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${lancamento.descricao}</td>
                <td>${formatarData(lancamento.dataVencimento)}</td>
                <td>${formatarData(lancamento.dataPagamento)}</td>
                <td class="valor ${lancamento.tipo === 'RECEITA' ? 'receita' : 'despesa'}">${formatarValor(lancamento.valor)}</td>
                <td>${lancamento.tipo}</td>
                <td>${lancamento.categoria.nome}</td>
                <td>${lancamento.pessoa.nome}</td>
                <td class="acoes">
                    <button class="btn-editar" data-id="${lancamento.codigo}">Editar</button>
                    <button class="btn-excluir" data-id="${lancamento.codigo}">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);

            // Adiciona eventos aos botões
            tr.querySelector('.btn-editar').addEventListener('click', () => editarLancamento(lancamento.codigo));
            tr.querySelector('.btn-excluir').addEventListener('click', () => excluirLancamento(lancamento.codigo));
        });
    } catch (error) {
        console.error('Erro ao carregar lançamentos:', error);
        alert('Erro ao carregar lançamentos. Verifique se a API está rodando.');
    }
}

async function salvarLancamento(event) {
    event.preventDefault();

    const descricao = document.getElementById('descricao').value;
    const dataVencimento = document.getElementById('dataVencimento').value;
    const dataPagamento = document.getElementById('dataPagamento').value || null;
    const valor = document.getElementById('valor').value;
    const tipo = document.getElementById('tipo').value;
    const categoriaId = document.getElementById('categoria').value;
    const pessoaId = document.getElementById('pessoa').value;
    const observacao = document.getElementById('observacao').value;

    const lancamentoId = document.getElementById('lancamento-form').dataset.id;

    const lancamento = {
        descricao,
        dataVencimento,
        dataPagamento,
        valor,
        tipo,
        observacao,
        categoria: {
            codigo: categoriaId
        },
        pessoa: {
            codigo: pessoaId
        }
    };

    try {
        let url = `${API_URL}/lancamentos`;
        let method = 'POST';

        // Se tiver ID, é uma atualização
        if (lancamentoId) {
            url = `${url}/${lancamentoId}`;
            method = 'PUT';
            lancamento.codigo = lancamentoId;
        }

        const response = await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(lancamento)
        });

        if (response.ok) {
            document.getElementById('lancamento-form').reset();
            document.getElementById('form-lancamento').style.display = 'none';
            document.getElementById('lancamento-form').dataset.id = '';
            carregarLancamentos();
        } else {
            const error = await response.json();
            alert(`Erro ao salvar lançamento: ${error.message || 'Erro desconhecido'}`);
        }
    } catch (error) {
        console.error('Erro ao salvar lançamento:', error);
        alert('Erro ao salvar lançamento. Verifique se a API está rodando.');
    }
}

async function editarLancamento(id) {
    try {
        const response = await fetch(`${API_URL}/lancamentos/${id}`);
        const lancamento = await response.json();

        document.getElementById('descricao').value = lancamento.descricao;
        document.getElementById('dataVencimento').value = lancamento.dataVencimento;
        document.getElementById('dataPagamento').value = lancamento.dataPagamento || '';
        document.getElementById('valor').value = lancamento.valor;
        document.getElementById('tipo').value = lancamento.tipo;
        document.getElementById('categoria').value = lancamento.categoria.codigo;
        document.getElementById('pessoa').value = lancamento.pessoa.codigo;
        document.getElementById('observacao').value = lancamento.observacao || '';

        document.getElementById('lancamento-form').dataset.id = lancamento.codigo;
        document.getElementById('form-lancamento').style.display = 'block';
    } catch (error) {
        console.error('Erro ao carregar lançamento para edição:', error);
        alert('Erro ao carregar lançamento para edição.');
    }
}

async function excluirLancamento(id) {
    if (!confirm('Tem certeza que deseja excluir este lançamento?')) return;

    try {
        const response = await fetch(`${API_URL}/lancamentos/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            carregarLancamentos();
        } else {
            alert('Erro ao excluir lançamento.');
        }
    } catch (error) {
        console.error('Erro ao excluir lançamento:', error);
        alert('Erro ao excluir lançamento.');
    }
}

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    // Carrega os dados iniciais
    carregarCategorias();
    carregarPessoas();
    carregarLancamentos();

    // Configura os formulários
    document.getElementById('categoria-form').addEventListener('submit', salvarCategoria);
    document.getElementById('pessoa-form').addEventListener('submit', salvarPessoa);
    document.getElementById('lancamento-form').addEventListener('submit', salvarLancamento);

    // Navegação entre abas
    document.getElementById('nav-lancamentos').addEventListener('click', function(e) {
        e.preventDefault();
        showSection('lancamentos');
    });

    document.getElementById('nav-pessoas').addEventListener('click', function(e) {
        e.preventDefault();
        showSection('pessoas');
    });

    document.getElementById('nav-categorias').addEventListener('click', function(e) {
        e.preventDefault();
        showSection('categorias');
    });

    // Mostrar/esconder formulários
    document.getElementById('btn-novo-lancamento').addEventListener('click', function() {
        document.getElementById('lancamento-form').reset();
        document.getElementById('lancamento-form').dataset.id = '';
        document.getElementById('form-lancamento').style.display = 'block';
    });

    document.getElementById('btn-cancelar-lancamento').addEventListener('click', function() {
        document.getElementById('form-lancamento').style.display = 'none';
    });

    document.getElementById('btn-nova-pessoa').addEventListener('click', function() {
        document.getElementById('pessoa-form').reset();
        document.getElementById('pessoa-form').dataset.id = '';
        document.getElementById('form-pessoa').style.display = 'block';
    });

    document.getElementById('btn-cancelar-pessoa').addEventListener('click', function() {
        document.getElementById('form-pessoa').style.display = 'none';
    });

    document.getElementById('btn-nova-categoria').addEventListener('click', function() {
        document.getElementById('categoria-form').reset();
        document.getElementById('categoria-form').dataset.id = '';
        document.getElementById('form-categoria').style.display = 'block';
    });

    document.getElementById('btn-cancelar-categoria').addEventListener('click', function() {
        document.getElementById('form-categoria').style.display = 'none';
    });
});

// Função para alternar entre seções
function showSection(sectionId) {
    // Esconde todas as seções
    document.querySelectorAll('main section').forEach(function(section) {
        section.style.display = 'none';
    });

    // Remove classe active de todos os links
    document.querySelectorAll('nav a').forEach(function(link) {
        link.classList.remove('active');
    });

    // Mostra a seção selecionada
    document.getElementById(sectionId).style.display = 'block';

    // Adiciona classe active ao link selecionado
    document.getElementById('nav-' + sectionId).classList.add('active');
}
