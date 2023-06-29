package too.planofinanceiro.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import too.planofinanceiro.dao.implementacao.InvestimentoDaoJDBC;
import too.planofinanceiro.entidades.Investimento;

public class IgInvestimentos extends JFrame {
	private JTable tableInvestimento;
	
	public IgInvestimentos(Connection conn) {
		String[] colunasTInvestimento = {"Objetivo", "Estratégia", "Nome", "Valor Investido", "Posição", "Rendimento Bruto", "Rentabilidade", "Vencimento"};
		
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Planejamento Financeiro - Investimentos");
		setBounds(100, 100, 1207, 521);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel orcamentoPanel = new JPanel();
		orcamentoPanel.setLayout(null);
		orcamentoPanel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		orcamentoPanel.setBorder(new TitledBorder(new LineBorder(new Color(160, 160, 160)), "Investimentos", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(105, 105, 105)));
		orcamentoPanel.setBackground(Color.WHITE);
		orcamentoPanel.setBounds(6, 82, 1179, 345);
		getContentPane().add(orcamentoPanel);
		
		JPanel TabelaPanel = new JPanel();
		TabelaPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		TabelaPanel.setBackground(Color.WHITE);
		TabelaPanel.setBounds(26, 31, 1130, 294);
		orcamentoPanel.add(TabelaPanel);
		TabelaPanel.setLayout(null);
		
		tableInvestimento = new JTable();
		tableInvestimento.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableInvestimento.setSelectionBackground(SystemColor.inactiveCaption);
		tableInvestimento.setShowVerticalLines(true);
		tableInvestimento.setModel(new DefaultTableModel(colunasTInvestimento, 12));
		tableInvestimento.getColumnModel().getColumn(0).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(0).setPreferredWidth(109);
		tableInvestimento.getColumnModel().getColumn(1).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(1).setPreferredWidth(53);
		tableInvestimento.getColumnModel().getColumn(2).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(2).setPreferredWidth(87);
		tableInvestimento.getColumnModel().getColumn(3).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(3).setPreferredWidth(231);
		tableInvestimento.getColumnModel().getColumn(4).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(4).setPreferredWidth(62);
		tableInvestimento.getColumnModel().getColumn(5).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(5).setPreferredWidth(73);
		tableInvestimento.getColumnModel().getColumn(6).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(6).setPreferredWidth(62);
		tableInvestimento.getColumnModel().getColumn(7).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(7).setPreferredWidth(73);
		List<Investimento> lista = new ArrayList<Investimento>();
		InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);		
		lista = investimentoDao.buscaCompleta();	
		DefaultTableModel tableModel = (DefaultTableModel) tableInvestimento.getModel();
		tableModel.setRowCount(0); 
		for (Investimento tab : lista) {
		    Object[] rowData = {tab.getObjetivo(), tab.getEstrategia(), tab.getNome(), tab.getValorInvestido(), tab.getPosicao(), tab.getRendimentoBruto(), tab.getRentabilidade(), tab.getVencimento()};
		    tableModel.addRow(rowData);
		}
		tableInvestimento.setShowHorizontalLines(true);
		
		JScrollPane scrollPane = new JScrollPane(tableInvestimento);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TabelaPanel.add(scrollPane, BorderLayout.CENTER);
		
		JButton btnGraficoBarras = new JButton("Gráfico em Barras");
		btnGraficoBarras.setBounds(802, 439, 133, 26);
		getContentPane().add(btnGraficoBarras);
		btnGraficoBarras.setMnemonic('B');
		btnGraficoBarras.setMnemonic(KeyEvent.VK_B);
		btnGraficoBarras.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGraficoBarras.setBackground(Color.WHITE);
		
		JButton btnGraficoEmColunas = new JButton("Gráfico em Colunas");
		btnGraficoEmColunas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnGraficoEmColunas.setBounds(947, 439, 139, 26);
		getContentPane().add(btnGraficoEmColunas);
		btnGraficoEmColunas.setMnemonic('c');
		btnGraficoEmColunas.setMnemonic(KeyEvent.VK_C);
		btnGraficoEmColunas.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGraficoEmColunas.setBackground(Color.WHITE);
		
		JButton btnFechar = new JButton("Fechar");
		btnFechar.setMnemonic('F');
		btnFechar.setMnemonic(KeyEvent.VK_F);
		btnFechar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnFechar.setBackground(Color.WHITE);
		btnFechar.setBounds(1098, 439, 87, 26);
		getContentPane().add(btnFechar);
		
		JLabel lblTotalInvestido = new JLabel("Total Investido");
		lblTotalInvestido.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalInvestido.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalInvestido.setForeground(new Color(0, 0, 139));
		lblTotalInvestido.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTotalInvestido.setBounds(37, 18, 156, 26);
		getContentPane().add(lblTotalInvestido);
		
		JLabel lblTotalAcumulado = new JLabel("Total Acumulado");
		lblTotalAcumulado.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalAcumulado.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalAcumulado.setForeground(new Color(0, 0, 139));
		lblTotalAcumulado.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTotalAcumulado.setBounds(247, 18, 156, 26);
		getContentPane().add(lblTotalAcumulado);
		
		JLabel lblRendimentoBruto = new JLabel("Rendimento Bruto");
		lblRendimentoBruto.setHorizontalTextPosition(SwingConstants.CENTER);
		lblRendimentoBruto.setHorizontalAlignment(SwingConstants.CENTER);
		lblRendimentoBruto.setForeground(new Color(0, 0, 139));
		lblRendimentoBruto.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRendimentoBruto.setBounds(477, 18, 182, 26);
		getContentPane().add(lblRendimentoBruto);
		
		JLabel lblTotalInvestidoValor = new JLabel("R$0,00");
		lblTotalInvestidoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalInvestidoValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalInvestidoValor.setForeground(Color.BLACK);
		lblTotalInvestidoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalInvestidoValor.setBounds(37, 44, 156, 26);
		getContentPane().add(lblTotalInvestidoValor);
		
		JLabel lblTotalAcumuladoValor = new JLabel("R$0,00");
		lblTotalAcumuladoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalAcumuladoValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalAcumuladoValor.setForeground(Color.BLACK);
		lblTotalAcumuladoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalAcumuladoValor.setBounds(247, 44, 156, 26);
		getContentPane().add(lblTotalAcumuladoValor);
		
		JLabel lblRendimentoBrutoValor = new JLabel("R$0,00");
		lblRendimentoBrutoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblRendimentoBrutoValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblRendimentoBrutoValor.setForeground(Color.BLACK);
		lblRendimentoBrutoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblRendimentoBrutoValor.setBounds(477, 44, 182, 26);
		getContentPane().add(lblRendimentoBrutoValor);
		
		setResizable(false);
		getContentPane().setLayout(null);
		setVisible(true);
	}
}
