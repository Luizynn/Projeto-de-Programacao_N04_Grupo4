package org.example.ui;

import org.example.model.Event;
import org.example.service.EventService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class EventListFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private EventService eventService = new EventService();

    public EventListFrame() {
        setTitle("Lista de Eventos");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 144, 255)); // Azul mais vibrante
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Eventos Disponíveis");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        topPanel.add(lblTitulo, BorderLayout.WEST);

        JButton btnAtualizar = createStyledButton("Atualizar",
                new Color(70, 130, 180),
                new Color(100, 149, 237));
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAtualizar.addActionListener(e -> carregarEventos());
        topPanel.add(btnAtualizar, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Configura tabela
        String[] colunas = {"Nome do Evento", "Localização", "Preço", "Comprar"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(200, 200, 200));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 144, 255));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setOpaque(true);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setForeground(Color.WHITE);
                label.setBackground(new Color(30, 144, 255));
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                return label;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);

        table.getColumn("Comprar").setCellRenderer(new ButtonRenderer());
        table.getColumn("Comprar").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));

        JLabel lblInfo = new JLabel("Clique em 'Comprar' para adquirir ingressos");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(127, 140, 141));
        bottomPanel.add(lblInfo);

        add(bottomPanel, BorderLayout.SOUTH);

        carregarEventos();
    }

    private JButton createStyledButton(String text, Color normalColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });

        return button;
    }

    private void carregarEventos() {
        tableModel.setRowCount(0);
        List<Event> eventos = eventService.listarEventos();

        for (Event e : eventos) {
            Object[] row = {
                    e.getName(),
                    e.getLocalizationAddress() + " - " + e.getLocalizationNeighborhood(),
                    String.format("R$ %.2f", e.getPrice()),
                    e // Passa o objeto Event para o botão
            };
            tableModel.addRow(row);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Comprar");
            setBackground(new Color(34, 139, 34)); // Verde floresta
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private Event currentEvent;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(34, 139, 34));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener(e -> {
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentEvent = (Event) value;
            button.setText("Comprar");
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                abrirPagamento(currentEvent);
            }
            clicked = false;
            return currentEvent;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    private void abrirPagamento(Event evento) {
        String userId = JOptionPane.showInputDialog(
                this,
                "Digite o ID do usuário para realizar a compra:",
                "Identificação do Usuário",
                JOptionPane.QUESTION_MESSAGE
        );

        if (userId == null || userId.trim().isEmpty()) {
            return;
        }

        try {
            Long idUsuario = Long.parseLong(userId.trim());

            new PaymentFrame(evento, idUsuario).setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "ID inválido! Digite apenas números.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EventListFrame().setVisible(true);
        });
    }
}