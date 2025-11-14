package org.example.ui;

import org.example.model.Event;
import org.example.service.PaymentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class PaymentFrame extends JFrame {
    
    private final Event event;
    private final Long userId; // Apenas o ID do usuário
    private PaymentService paymentService;
    
    // Componentes de método de pagamento
    private JRadioButton rbCreditCard, rbPix, rbBoleto;
    private ButtonGroup paymentMethodGroup;
    
    // Componentes de cartão
    private JPanel cardPanel;
    private JTextField txtCardNumber, txtCardName, txtCVV;
    private JComboBox<String> cmbMonth, cmbYear;
    
    // Componentes de voucher e valores
    private JTextField txtVoucher;
    private JButton btnApplyVoucher;
    private JLabel lblSubtotal, lblFee, lblDiscount, lblTotal;
    
    // Valores do pedido
    private double subtotal;
    private double fee = 0.0;
    private double discount = 0.0;
    private double total;
    
    private String appliedVoucher = null;
    
    public PaymentFrame(Event event, Long userId) {
        this.event = event;
        this.userId = userId;
        this.paymentService = new PaymentService();
        this.subtotal = event.getPrice();
        this.total = subtotal;
        
        initComponents();
        calculateTotals();
    }
    
    private void initComponents() {
        setTitle("Pagamento - " + event.getName());
        setSize(800, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Painel principal com scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Header
        mainPanel.add(createHeaderPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Informações do evento
        mainPanel.add(createEventInfoPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Método de pagamento
        mainPanel.add(createPaymentMethodPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Dados do cartão (inicialmente visível)
        cardPanel = createCardPanel();
        mainPanel.add(cardPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Voucher
        mainPanel.add(createVoucherPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Resumo do pedido
        mainPanel.add(createSummaryPanel());
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Botões de ação
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel lblTitle = new JLabel("💳 Finalizar Pagamento");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        
        panel.add(lblTitle, BorderLayout.WEST);
        return panel;
    }
    
    private JPanel createEventInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Detalhes do Evento",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        JLabel lblEventName = new JLabel("📅 " + event.getName());
        lblEventName.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel lblLocation = new JLabel("📍 " + event.getLocalizationAddress() + 
                                       " - " + event.getLocalizationNeighborhood());
        lblLocation.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel lblPrice = new JLabel("💵 Valor: R$ " + 
                                    String.format("%.2f", event.getPrice()));
        lblPrice.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrice.setForeground(new Color(39, 174, 96));
        
        panel.add(lblEventName);
        panel.add(lblLocation);
        panel.add(lblPrice);
        
        return panel;
    }
    
    private JPanel createPaymentMethodPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Método de Pagamento",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        paymentMethodGroup = new ButtonGroup();
        
        rbCreditCard = new JRadioButton("💳 Cartão de Crédito (+5% taxa)");
        rbCreditCard.setFont(new Font("Arial", Font.PLAIN, 14));
        rbCreditCard.setBackground(Color.WHITE);
        rbCreditCard.setSelected(true);
        rbCreditCard.addActionListener(e -> onPaymentMethodChanged());
        
        rbPix = new JRadioButton("📱 PIX (-10% desconto)");
        rbPix.setFont(new Font("Arial", Font.PLAIN, 14));
        rbPix.setBackground(Color.WHITE);
        rbPix.addActionListener(e -> onPaymentMethodChanged());
        
        rbBoleto = new JRadioButton("🧾 Boleto Bancário");
        rbBoleto.setFont(new Font("Arial", Font.PLAIN, 14));
        rbBoleto.setBackground(Color.WHITE);
        rbBoleto.addActionListener(e -> onPaymentMethodChanged());
        
        paymentMethodGroup.add(rbCreditCard);
        paymentMethodGroup.add(rbPix);
        paymentMethodGroup.add(rbBoleto);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(rbCreditCard);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(rbPix);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(rbBoleto);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        return panel;
    }
    
    private JPanel createCardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Dados do Cartão",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Número do cartão
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Número do Cartão:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtCardNumber = new JTextField();
        txtCardNumber.setFont(new Font("Monospaced", Font.PLAIN, 14));
        setCardNumberMask(txtCardNumber);
        panel.add(txtCardNumber, gbc);
        
        // Nome no cartão
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Nome no Cartão:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtCardName = new JTextField();
        txtCardName.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(txtCardName, gbc);
        
        // Validade
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Validade:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 1;
        String[] months = {"01", "02", "03", "04", "05", "06", 
                          "07", "08", "09", "10", "11", "12"};
        cmbMonth = new JComboBox<>(months);
        cmbMonth.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(cmbMonth, gbc);
        
        gbc.gridx = 2;
        String[] years = new String[15];
        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 15; i++) {
            years[i] = String.valueOf(currentYear + i);
        }
        cmbYear = new JComboBox<>(years);
        cmbYear.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(cmbYear, gbc);
        
        // CVV
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("CVV:"), gbc);
        
        gbc.gridx = 1;
        txtCVV = new JTextField();
        txtCVV.setFont(new Font("Monospaced", Font.PLAIN, 14));
        setCVVMask(txtCVV);
        panel.add(txtCVV, gbc);
        
        return panel;
    }
    
    private JPanel createVoucherPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Cupom de Desconto",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        txtVoucher = new JTextField();
        txtVoucher.setFont(new Font("Arial", Font.PLAIN, 14));
        txtVoucher.setPreferredSize(new Dimension(300, 35));
        
        btnApplyVoucher = new JButton("Aplicar");
        btnApplyVoucher.setBackground(new Color(46, 204, 113));
        btnApplyVoucher.setForeground(Color.WHITE);
        btnApplyVoucher.setFont(new Font("Arial", Font.BOLD, 12));
        btnApplyVoucher.setFocusPainted(false);
        btnApplyVoucher.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApplyVoucher.addActionListener(e -> applyVoucher());
        
        inputPanel.add(txtVoucher, BorderLayout.CENTER);
        inputPanel.add(btnApplyVoucher, BorderLayout.EAST);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Resumo do Pedido",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font valueFont = new Font("Arial", Font.BOLD, 14);
        Font totalFont = new Font("Arial", Font.BOLD, 18);
        
        lblSubtotal = new JLabel("R$ 0,00");
        lblSubtotal.setFont(valueFont);
        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
        
        lblFee = new JLabel("R$ 0,00");
        lblFee.setFont(valueFont);
        lblFee.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFee.setForeground(new Color(231, 76, 60));
        
        lblDiscount = new JLabel("R$ 0,00");
        lblDiscount.setFont(valueFont);
        lblDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDiscount.setForeground(new Color(39, 174, 96));
        
        lblTotal = new JLabel("R$ 0,00");
        lblTotal.setFont(totalFont);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setForeground(new Color(41, 128, 185));
        
        JLabel lbl1 = new JLabel("Subtotal:");
        lbl1.setFont(labelFont);
        panel.add(lbl1);
        panel.add(lblSubtotal);
        
        JLabel lbl2 = new JLabel("Taxa/Desconto do Método:");
        lbl2.setFont(labelFont);
        panel.add(lbl2);
        panel.add(lblFee);
        
        JLabel lbl3 = new JLabel("Desconto (Voucher):");
        lbl3.setFont(labelFont);
        panel.add(lbl3);
        panel.add(lblDiscount);
        
        panel.add(new JSeparator());
        panel.add(new JSeparator());
        
        JLabel lbl4 = new JLabel("TOTAL:");
        lbl4.setFont(totalFont);
        panel.add(lbl4);
        panel.add(lblTotal);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setBackground(new Color(189, 195, 199));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        
        JButton btnConfirm = new JButton("💰 Confirmar Pagamento");
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirm.setPreferredSize(new Dimension(220, 40));
        btnConfirm.setBackground(new Color(39, 174, 96));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(e -> processPayment());
        
        panel.add(btnCancel);
        panel.add(btnConfirm);
        
        return panel;
    }
    
    private void onPaymentMethodChanged() {
        cardPanel.setVisible(rbCreditCard.isSelected());
        calculateTotals();
    }
    
    private void applyVoucher() {
        String voucherCode = txtVoucher.getText().trim();
        
        if (voucherCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Digite um código de voucher válido",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simula validação do voucher (você deve integrar com o backend)
        // Por enquanto, vamos aceitar qualquer código e dar 15% de desconto
        appliedVoucher = voucherCode;
        discount = (subtotal + fee) * 0.15;
        
        calculateTotals();
        
        JOptionPane.showMessageDialog(this,
            "Voucher aplicado com sucesso!\nDesconto de 15%",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE);
        
        txtVoucher.setEnabled(false);
        btnApplyVoucher.setEnabled(false);
    }
    
    private void calculateTotals() {
        // Calcula taxa baseada no método de pagamento
        if (rbCreditCard.isSelected()) {
            fee = subtotal * 0.05;
        } else if (rbPix.isSelected()) {
            fee = subtotal * -0.10;
        } else {
            fee = 0.0;
        }
        
        // Calcula total
        total = subtotal + fee - discount;
        
        // Atualiza labels
        lblSubtotal.setText(String.format("R$ %.2f", subtotal));
        lblFee.setText(String.format("R$ %.2f", fee));
        lblDiscount.setText(String.format("R$ %.2f", discount));
        lblTotal.setText(String.format("R$ %.2f", total));
    }
    
    private void processPayment() {
        // Valida dados do cartão se necessário
        if (rbCreditCard.isSelected()) {
            if (!validateCardData()) {
                return;
            }
        }
        
        // Prepara dados para envio
        String paymentMethod = getSelectedPaymentMethod();
        String cardNumber = rbCreditCard.isSelected() ? 
                           txtCardNumber.getText().replaceAll("\\s", "") : null;
        String month = rbCreditCard.isSelected() ? 
                      (String) cmbMonth.getSelectedItem() : null;
        String year = rbCreditCard.isSelected() ? 
                     (String) cmbYear.getSelectedItem() : null;
        String cvv = rbCreditCard.isSelected() ? txtCVV.getText() : null;
        
        // Mostra loading
        JDialog loadingDialog = showLoadingDialog();
        
        // Processa pagamento em background
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    return paymentService.createPayment(
                        userId, // Usa o ID do usuário
                        paymentMethod,
                        List.of(event.getId()),
                        appliedVoucher,
                        cardNumber,
                        month,
                        year,
                        cvv
                    );
                } catch (Exception e) {
                    throw e;
                }
            }
            
            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    if (get()) {
                        showSuccessMessage();
                        dispose();
                    } else {
                        showErrorMessage("Falha ao processar pagamento");
                    }
                } catch (Exception e) {
                    showErrorMessage(e.getMessage());
                }
            }
        };
        
        worker.execute();
    }
    
    private boolean validateCardData() {
        String cardNumber = txtCardNumber.getText().replaceAll("\\s", "");
        String cardName = txtCardName.getText().trim();
        String month = (String) cmbMonth.getSelectedItem();
        String year = (String) cmbYear.getSelectedItem();
        String cvv = txtCVV.getText();
        
        if (cardNumber.isEmpty() || cardName.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Preencha todos os campos do cartão",
                "Dados Incompletos",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validação básica do número (apenas dígitos e tamanho)
        if (cardNumber.length() < 13 || cardNumber.length() > 19) {
            JOptionPane.showMessageDialog(this,
                "Número do cartão inválido",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validação básica do CVV
        if (cvv.length() < 3 || cvv.length() > 4) {
            JOptionPane.showMessageDialog(this,
                "CVV inválido",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private String getSelectedPaymentMethod() {
        if (rbCreditCard.isSelected()) return "CREDIT_CARD";
        if (rbPix.isSelected()) return "PIX";
        return "BOLETO";
    }
    
    private JDialog showLoadingDialog() {
        JDialog dialog = new JDialog(this, "Processando", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblMessage = new JLabel("Processando pagamento...", SwingConstants.CENTER);
        lblMessage.setFont(new Font("Arial", Font.BOLD, 14));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        panel.add(lblMessage, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        dialog.add(panel);
        
        new Thread(() -> dialog.setVisible(true)).start();
        
        return dialog;
    }
    
    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(this,
            "Pagamento realizado com sucesso!\n\n" +
            "Evento: " + event.getName() + "\n" +
            "Valor pago: R$ " + String.format("%.2f", total),
            "Pagamento Confirmado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this,
            "Erro ao processar pagamento:\n" + message,
            "Erro",
            JOptionPane.ERROR_MESSAGE);
    }
    
    // Métodos auxiliares para máscaras
    private void setCardNumberMask(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, 
                              String text, AttributeSet attrs) throws BadLocationException {
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = current.substring(0, offset) + text + 
                               current.substring(offset + length);
                String digits = newText.replaceAll("\\D", "");
                
                if (digits.length() <= 16) {
                    String formatted = digits.replaceAll("(.{4})", "$1 ").trim();
                    super.replace(fb, 0, fb.getDocument().getLength(), 
                                formatted, attrs);
                }
            }
        });
    }
    
    private void setCVVMask(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, 
                              String text, AttributeSet attrs) throws BadLocationException {
                String newText = text.replaceAll("\\D", "");
                if (fb.getDocument().getLength() + newText.length() - length <= 4) {
                    super.replace(fb, offset, length, newText, attrs);
                }
            }
        });
    }
}