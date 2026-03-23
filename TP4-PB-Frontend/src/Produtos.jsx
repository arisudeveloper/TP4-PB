import React, { useState, useEffect } from 'react'
import './Produtos.css'
import api from './services/api'

const Produtos = () => {
    const [produtos, setProdutos] = useState([])
    const [nome, setNome] = useState('')
    const [quantidade, setQuantidade] = useState('')
    const [preco, setPreco] = useState('')
    const [carregando, setCarregando] = useState(false)

    async function listarProdutos() {
        setCarregando(true)
        try {
            const response = await api.get('/produtos')
            if (Array.isArray(response.data)) {
                setProdutos(response.data)
            } else {
                setProdutos([])
            }
        } catch (error) {
            console.error("Erro ao buscar produtos:", error)
            setProdutos([]) 
        } finally {
            setCarregando(false)
        }
    }

    async function cadastrarProduto() {
        if (!nome || !quantidade || !preco) {
            alert('preencha todos os campos')
            return
        }

        try {
            await api.post('/produtos', {
                nome,
                quantidade: Number(quantidade),
                preco: Number(preco)
            })
            limparCampos()
            listarProdutos()
        } catch (error) {
            alert('erro ao cadastrar')
        }
    }

    async function excluirProduto(id) {
        try {
            await api.delete(`/produtos/${id}`)
            listarProdutos()
        } catch (error) {
            alert('erro ao excluir')
        }
    }

    async function atualizarProduto(id) {
        if (!nome || !quantidade || !preco) {
            alert('preencha os campos acima para atualizar')
            return
        }

        try {
            await api.put(`/produtos/${id}`, {
                nome,
                quantidade: Number(quantidade),
                preco: Number(preco)
            })
            limparCampos()
            listarProdutos()
        } catch (error) {
            alert('erro ao atualizar')
        }
    }

    function limparCampos() {
        setNome('')
        setQuantidade('')
        setPreco('')
    }

    useEffect(() => {
        listarProdutos()
    }, [])

    return (
        <div className='containerProdutos'>
            <h2>Cadastro de Produtos</h2>

            {/* Inputs com data-testid para o Selenium encontrar facilmente */}
            <input 
                data-testid="input-nome"
                type="text"
                placeholder="nome"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
            />

            <input 
                data-testid="input-quantidade"
                type="number"
                placeholder="quantidade"
                value={quantidade}
                onChange={(e) => setQuantidade(e.target.value)}
            />

            <input 
                data-testid="input-preco"
                type="number"
                placeholder="preço"
                value={preco}
                onChange={(e) => setPreco(e.target.value)}
            />

            <button data-testid="btn-cadastrar" onClick={cadastrarProduto}>
                cadastrar
            </button>

            <hr />

            <h3>Lista de Produtos</h3>

            {carregando && <p data-testid="loading">Carregando produtos...</p>}

            {!carregando && produtos.length === 0 && (
                <p data-testid="msg-vazia">Nenhum produto cadastrado</p>
            )}

            <div data-testid="lista-produtos">
                {produtos.map((p) => (
                    <div key={p.id} className="produto-item" data-testid={`item-produto-${p.id}`}>
                        <p>
                            <span data-testid={`nome-produto-${p.id}`}>{p.nome}</span> - 
                            <span> {p.quantidade}</span> - 
                            R$ <span>{p.preco}</span>
                        </p>

                        <button 
                            data-testid={`btn-excluir-${p.id}`}
                            onClick={() => excluirProduto(p.id)}
                        >
                            excluir
                        </button>

                        <button 
                            data-testid={`btn-atualizar-${p.id}`}
                            onClick={() => atualizarProduto(p.id)}
                        >
                            atualizar com os dados acima
                        </button>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default Produtos