package org.aps;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InterfaceUsuario {

    private JFrame janela;
    private JPanel painelPrincipal;
    private JTabbedPane guiaPainel;
    private BancoDados bancoDados;
    private DefaultTableModel modeloTabela;
    private JTable tabelaUsuarios;
    private boolean usuarioAutenticado = false;
    private JTextField campoFiltro;

    public InterfaceUsuario(GerenciamentoUsuariosApp app) {
        bancoDados = new BancoDados();
        inicializar();
        criarInterface();
    }

    private void inicializar() {
        janela = new JFrame("Gerenciamento de Usuários");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(800, 600);
        janela.setLocationRelativeTo(null);
    }

    private void criarInterface() {
        painelPrincipal = new JPanel(new BorderLayout());
        janela.setContentPane(painelPrincipal);

        JPanel painelMenu = new JPanel(new GridLayout(0, 1));
        JButton botaoLogin = new JButton("Login");
        JButton botaoAdicionarUsuario = new JButton("Adicionar Usuário");
        JButton botaoListarUsuarios = new JButton("Listar Usuários");

        botaoLogin.addActionListener(e -> mostrarPainelLogin());
        botaoAdicionarUsuario.addActionListener(e -> {
            if (usuarioAutenticado) {
                mostrarPainelAdicionarUsuario();
            } else {
                JOptionPane.showMessageDialog(janela, "Você precisa estar logado para adicionar um usuário.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            }
        });
        botaoListarUsuarios.addActionListener(e -> {
            if (usuarioAutenticado) {
                mostrarPainelListaUsuarios();
            } else {
                JOptionPane.showMessageDialog(janela, "Você precisa estar logado para listar usuários.", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            }
        });

        painelMenu.add(botaoLogin);
        painelMenu.add(botaoAdicionarUsuario);
        painelMenu.add(botaoListarUsuarios);

        painelPrincipal.add(painelMenu, BorderLayout.WEST);
        guiaPainel = new JTabbedPane();
        painelPrincipal.add(guiaPainel, BorderLayout.CENTER);

        janela.setVisible(true);
    }

    private void mostrarPainelLogin() {
        JPanel painelLogin = criarPainelLogin();
        guiaPainel.removeAll();
        guiaPainel.add("Login", painelLogin);
    }

    private JPanel criarPainelLogin() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel rotuloTitulo = new JLabel("Login");
        rotuloTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        rotuloTitulo.setForeground(Color.WHITE);

        JLabel rotuloNomeUsuario = new JLabel("Nome de Usuário:");
        rotuloNomeUsuario.setForeground(Color.WHITE);
        JTextField campoNomeUsuario = new JTextField(20);

        JLabel rotuloSenha = new JLabel("Senha:");
        rotuloSenha.setForeground(Color.WHITE);
        JPasswordField campoSenha = new JPasswordField(20);

        JButton botaoLogin = new JButton("Entrar");
        JButton botaoCriarUsuario = new JButton("Criar Usuário");

        botaoLogin.addActionListener(e -> {
            String nomeUsuario = campoNomeUsuario.getText();
            String senha = new String(campoSenha.getPassword());
            if (bancoDados.autenticarUsuario(nomeUsuario, senha)) {
                JOptionPane.showMessageDialog(janela, "Login bem-sucedido!");
                usuarioAutenticado = true;
                mostrarPainelListaUsuarios();
            } else {
                JOptionPane.showMessageDialog(janela, "Nome de usuário ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botaoCriarUsuario.addActionListener(e -> mostrarPainelAdicionarUsuario());

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painel.add(rotuloTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        painel.add(rotuloNomeUsuario, gbc);

        gbc.gridx = 1;
        painel.add(campoNomeUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        painel.add(rotuloSenha, gbc);

        gbc.gridx = 1;
        painel.add(campoSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        painel.add(botaoLogin, gbc);

        gbc.gridy++;
        painel.add(botaoCriarUsuario, gbc);

        return painel;
    }

    private void mostrarPainelAdicionarUsuario() {
        JPanel painelAdicionarUsuario = criarPainelAdicionarUsuario();
        guiaPainel.removeAll();
        guiaPainel.add("Adicionar Usuário", painelAdicionarUsuario);
    }

    private JPanel criarPainelAdicionarUsuario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel rotuloTitulo = new JLabel("Adicionar Usuário");
        rotuloTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        rotuloTitulo.setForeground(Color.WHITE);

        JLabel rotuloNomeUsuario = new JLabel("Nome de Usuário:");
        rotuloNomeUsuario.setForeground(Color.WHITE);
        JTextField campoNomeUsuario = new JTextField(20);
        campoNomeUsuario.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel rotuloSenha = new JLabel("Senha:");
        rotuloSenha.setForeground(Color.WHITE);
        JPasswordField campoSenha = new JPasswordField(20);
        campoSenha.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JButton botaoAdicionar = new JButton("Adicionar Usuário");
        botaoAdicionar.setBackground(new Color(0, 123, 255));
        botaoAdicionar.setForeground(Color.WHITE);
        botaoAdicionar.setFocusPainted(false);

        botaoAdicionar.addActionListener(e -> {
            String nomeUsuario = campoNomeUsuario.getText();
            String senha = new String(campoSenha.getPassword());
            bancoDados.adicionarUsuario(nomeUsuario, senha);
            JOptionPane.showMessageDialog(janela, "Usuário adicionado com sucesso!");
            campoNomeUsuario.setText("");
            campoSenha.setText("");
        });

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painel.add(rotuloTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        painel.add(rotuloNomeUsuario, gbc);

        gbc.gridx = 1;
        painel.add(campoNomeUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        painel.add(rotuloSenha, gbc);

        gbc.gridx = 1;
        painel.add(campoSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        painel.add(botaoAdicionar, gbc);

        return painel;
    }

    private void mostrarPainelListaUsuarios() {
        JPanel painelLista = criarPainelListaUsuarios();
        guiaPainel.removeAll();
        guiaPainel.add("Listar Usuários", painelLista);
    }

    private JPanel criarPainelListaUsuarios() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(50, 50, 50));

        String[] colunas = {"ID", "Nome de Usuário", "Data de Última Modificação"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setBackground(new Color(40, 40, 40));
        tabelaUsuarios.setForeground(Color.WHITE);
        tabelaUsuarios.setFillsViewportHeight(true);

        campoFiltro = new JTextField();
        campoFiltro.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        campoFiltro.addActionListener(e -> aplicarFiltro());

        atualizarTabela();

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoEditar = new JButton("Editar Usuário");
        JButton botaoDeletar = new JButton("Deletar Usuário");

        botaoEditar.addActionListener(e -> editarUsuario());
        botaoDeletar.addActionListener(e -> deletarUsuario());

        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoDeletar);

        painel.add(new JLabel("Filtrar:"), BorderLayout.NORTH);
        painel.add(campoFiltro, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaUsuarios), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private void aplicarFiltro() {
        String filterText = campoFiltro.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabela);
        sorter.setRowFilter(RowFilter.regexFilter(filterText));
        tabelaUsuarios.setRowSorter(sorter);
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        ResultSet rs = bancoDados.listarUsuarios();
        try {
            while (rs != null && rs.next()) {
                int id = rs.getInt("id");
                String nomeUsuario = rs.getString("nome_usuario");
                String dataUltimaModificacao = rs.getString("data_ultima_modificacao");
                modeloTabela.addRow(new Object[]{id, nomeUsuario, dataUltimaModificacao});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editarUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) modeloTabela.getValueAt(selectedRow, 0);
            String nomeUsuario = JOptionPane.showInputDialog(janela, "Novo Nome de Usuário:");
            String senha = JOptionPane.showInputDialog(janela, "Nova Senha:");
            if (nomeUsuario != null && senha != null) {
                bancoDados.editarUsuario(id, nomeUsuario, senha);
                atualizarTabela();
                JOptionPane.showMessageDialog(janela, "Usuário editado com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(janela, "Selecione um usuário para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) modeloTabela.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(janela, "Tem certeza que deseja deletar este usuário?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bancoDados.deletarUsuario(id);
                atualizarTabela();
                JOptionPane.showMessageDialog(janela, "Usuário deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(janela, "Selecione um usuário para deletar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
