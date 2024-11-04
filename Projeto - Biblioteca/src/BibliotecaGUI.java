// BibliotecaGUI.java
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe que representa a interface gráfica da biblioteca.
 */
public class BibliotecaGUI extends JFrame {
    private JTextArea textArea;
    private Membro usuarioAtual;

    /**
     * Construtor da classe BibliotecaGUI.
     */
    public BibliotecaGUI() {
        setTitle("Sistema de Gerenciamento de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Área de texto para exibir informações
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(20, 150, 740, 400);
        add(scrollPane);

        // Inicia a tela de login
        telaLogin();
    }

    /**
     * Método para exibir a tela de login.
     */
    private void telaLogin() {
        JTextField txtId = new JTextField();
        JTextField txtNome = new JTextField();
        Object[] message = {
                "ID:", txtId,
                "Nome:", txtNome
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = txtId.getText();
            String nome = txtNome.getText();
            usuarioAtual = Biblioteca.getInstance().autenticarUsuario(id, nome);
            if (usuarioAtual != null) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuarioAtual.getNome() + "!");
                menuPrincipal();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
                telaLogin();
            }
        } else {
            System.exit(0);
        }
    }

    /**
     * Método para exibir o menu principal após o login.
     */
    private void menuPrincipal() {
        // Botões de ação
        JButton btnListarItens = new JButton("Listar Itens");
        JButton btnEmprestarItem = new JButton("Emprestar Item");
        JButton btnDevolverItem = new JButton("Devolver Item");
        JButton btnSalvarDados = new JButton("Salvar Dados");
        JButton btnCarregarDados = new JButton("Carregar Dados");

        // Definição de posições dos botões
        btnListarItens.setBounds(20, 20, 150, 30);
        btnEmprestarItem.setBounds(180, 20, 150, 30);
        btnDevolverItem.setBounds(340, 20, 150, 30);
        btnSalvarDados.setBounds(500, 20, 150, 30);
        btnCarregarDados.setBounds(660, 20, 150, 30);

        // Adiciona os botões à interface
        add(btnListarItens);
        add(btnEmprestarItem);
        add(btnDevolverItem);
        add(btnSalvarDados);
        add(btnCarregarDados);

        // Verifica se o usuário é bibliotecário para liberar funções extras
        if (usuarioAtual instanceof Bibliotecario) {
            JButton btnAdicionarItem = new JButton("Adicionar Item");
            JButton btnRemoverItem = new JButton("Remover Item");
            btnAdicionarItem.setBounds(20, 70, 150, 30);
            btnRemoverItem.setBounds(180, 70, 150, 30);
            add(btnAdicionarItem);
            add(btnRemoverItem);

            // Ações dos botões de bibliotecário
            btnAdicionarItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    adicionarItem();
                }
            });

            btnRemoverItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removerItem();
                }
            });
        }

        // Ações dos botões comuns
        btnListarItens.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarItens();
            }
        });

        btnEmprestarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emprestarItem();
            }
        });

        btnDevolverItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                devolverItem();
            }
        });

        btnSalvarDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Biblioteca.getInstance().salvarDados();
            }
        });

        btnCarregarDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Biblioteca.getInstance().carregarDados();
            }
        });
    }

    /**
     * Método para listar os itens na área de texto.
     */
    private void listarItens() {
        textArea.setText("");
        for (Item item : Biblioteca.getInstance().listaItens) {
            textArea.append(item.getTitulo() + " - " + item.getId() + "\n");
        }
    }

    /**
     * Método para adicionar um item (apenas para bibliotecários).
     */
    private void adicionarItem() {
        String[] opcoes = {"Livro", "Revista"};
        String tipo = (String) JOptionPane.showInputDialog(this, "Selecione o tipo de item:", "Adicionar Item",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (tipo != null) {
            if (tipo.equals("Livro")) {
                adicionarLivro();
            } else if (tipo.equals("Revista")) {
                adicionarRevista();
            }
        }
    }

    /**
     * Método para adicionar um livro.
     */
    private void adicionarLivro() {
        try {
            String id = JOptionPane.showInputDialog("ID do Livro:");
            String titulo = JOptionPane.showInputDialog("Título do Livro:");
            int ano = Integer.parseInt(JOptionPane.showInputDialog("Ano de Publicação:"));
            String autor = JOptionPane.showInputDialog("Autor:");
            String editora = JOptionPane.showInputDialog("Editora:");

            if (id.isEmpty() || titulo.isEmpty() || autor.isEmpty() || editora.isEmpty()) {
                throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
            }

            Livro livro = new Livro(id, titulo, ano, autor, editora);
            Biblioteca.getInstance().adicionarItem(livro);
            JOptionPane.showMessageDialog(this, "Livro adicionado com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano de publicação inválido.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * Método para adicionar uma revista.
     */
    private void adicionarRevista() {
        try {
            String id = JOptionPane.showInputDialog("ID da Revista:");
            String titulo = JOptionPane.showInputDialog("Título da Revista:");
            int ano = Integer.parseInt(JOptionPane.showInputDialog("Ano de Publicação:"));
            int numeroEdicao = Integer.parseInt(JOptionPane.showInputDialog("Número da Edição:"));
            String mesPublicacao = JOptionPane.showInputDialog("Mês de Publicação:");

            if (id.isEmpty() || titulo.isEmpty() || mesPublicacao.isEmpty()) {
                throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
            }

            Revista revista = new Revista(id, titulo, ano, numeroEdicao, mesPublicacao);
            Biblioteca.getInstance().adicionarItem(revista);
            JOptionPane.showMessageDialog(this, "Revista adicionada com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dados numéricos inválidos.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * Método para remover um item (apenas para bibliotecários).
     */
    private void removerItem() {
        String idItem = JOptionPane.showInputDialog("Informe o ID do item a ser removido:");
        if (Biblioteca.getInstance().removerItem(idItem)) {
            JOptionPane.showMessageDialog(this, "Item removido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Item não encontrado.");
        }
    }

    /**
     * Método para emprestar um item.
     */
    private void emprestarItem() {
        String idItem = JOptionPane.showInputDialog("Informe o ID do item para empréstimo:");
        Item item = Biblioteca.getInstance().buscarItem(idItem);
        if (item != null) {
            try {
                usuarioAtual.emprestarItem(item);
                JOptionPane.showMessageDialog(this, "Item emprestado com sucesso!");
            } catch (ItemIndisponivelException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Item não encontrado.");
        }
    }

    /**
     * Método para devolver um item.
     */
    private void devolverItem() {
        String idItem = JOptionPane.showInputDialog("Informe o ID do item para devolução:");
        Item item = null;
        for (Item i : usuarioAtual.itensEmprestados) {
            if (i.getId().equals(idItem)) {
                item = i;
                break;
            }
        }
        if (item != null) {
            usuarioAtual.devolverItem(item);
            JOptionPane.showMessageDialog(this, "Item devolvido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Item não encontrado nos seus empréstimos.");
        }
    }

    /**
     * Método principal para executar a interface gráfica.
     */
    public static void main(String[] args) {
        Biblioteca.getInstance().carregarDados(); // Carrega os dados ao iniciar o programa
        BibliotecaGUI gui = new BibliotecaGUI();
        gui.setVisible(true);
    }
}
