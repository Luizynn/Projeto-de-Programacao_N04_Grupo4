package main.ui;

import model.Event;
import service.EventService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EventListFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private EventService eventService = new EventService();

    public EventListFrame() {
        setTitle("Lista de Eventos");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] colunas = {"Nome", "Localização", "Preço", "Ações"};
        tableModel = new DefaultTableModel(colunas, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarEventos());
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarEventos();
    }

    private void carregarEventos() {
        tableModel.setRowCount(0);
        List<Event> eventos = eventService.listarEventos();

        for (Event e : eventos) {
            JButton btnComprar = new JButton("Comprar");
            btnComprar.addActionListener(ev -> abrirPagamento(e));

            Object[] row = {
                    e.getName(),
                    e.getLocalizationAddress() + " - " + e.getLocalizationNeighborhood(),
                    "R$ " + e.getPrice(),
                    btnComprar.getText()
            };
            tableModel.addRow(row);
        }
    }

    private void abrirPagamento(Event e) {
        JOptionPane.showMessageDialog(this,
                "Abrindo módulo de pagamento para: " + e.getName(),
                "Pagamento",
                JOptionPane.INFORMATION_MESSAGE);
        // aqui depois você chama o frame do módulo de pagamento
    }
}
