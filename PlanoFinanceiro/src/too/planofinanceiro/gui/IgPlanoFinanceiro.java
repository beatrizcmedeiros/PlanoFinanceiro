package too.planofinanceiro.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Paint;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import mos.io.InputOutput;
import mos.reader.Line;
import too.planofinanceiro.arquivo.Arquivo;
import too.planofinanceiro.arquivo.ArquivoDespesa;
import too.planofinanceiro.arquivo.ArquivoInvestimento;
import too.planofinanceiro.arquivo.ArquivoReceita;
import too.planofinanceiro.dao.implementacao.CategoriaDaoJDBC;
import too.planofinanceiro.dao.implementacao.InvestimentoDaoJDBC;
import too.planofinanceiro.dao.implementacao.OrcamentoDaoJDBC;
import too.planofinanceiro.dao.implementacao.RendaMensalDaoJDBC;
import too.planofinanceiro.entidades.Categoria;
import too.planofinanceiro.entidades.TabelaOrcamento;
import too.planofinanceiro.util.InsereAlteraOrcamentoPelaTabela;

public class IgPlanoFinanceiro extends JFrame {
	private JTable tableOrcamento;
	
	public IgPlanoFinanceiro(Connection conn) {
		String[] colunasTDespesa = {"Data", "Dia", "Tipo", "Descrição", "Valor", "Paga"};
		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');
        DecimalFormat formatador = new DecimalFormat("#,##0.00", simbolos);
        
        Calendar calendario = Calendar.getInstance();
        int mes = calendario.get(Calendar.MONTH) + 1; 
        int ano = calendario.get(Calendar.YEAR);
        		
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Planejamento Financeiro");
		setBounds(100, 100, 1207, 521);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnImportar = new JButton("Importar");
		btnImportar.setMnemonic('o');
		btnImportar.setMnemonic(KeyEvent.VK_O);
		btnImportar.setBackground(new Color(255, 255, 255));
		btnImportar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnImportar.setMnemonic('o');
		btnImportar.setMnemonic(KeyEvent.VK_O);
		btnImportar.setBounds(943, 444, 106, 26);
		getContentPane().add(btnImportar);
		
		JButton btnInvestimentos = new JButton("Investimentos");
		btnInvestimentos.setBackground(new Color(255, 255, 255));
		btnInvestimentos.setMnemonic('I');
		btnInvestimentos.setMnemonic(KeyEvent.VK_I);
		btnInvestimentos.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnInvestimentos.setBounds(1056, 444, 123, 26);
		getContentPane().add(btnInvestimentos);
		
		JPanel orcamentoPanel = new JPanel();
		orcamentoPanel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		orcamentoPanel.setBackground(new Color(255, 255, 255));
		orcamentoPanel.setBorder(new TitledBorder(new LineBorder(new Color(160, 160, 160)), "Or\u00E7amento", TitledBorder.LEFT, TitledBorder.TOP, null, SystemColor.controlDkShadow));
		orcamentoPanel.setBounds(16, 96, 1163, 345);
		getContentPane().add(orcamentoPanel);
		orcamentoPanel.setLayout(null);
		
		JLabel lblMes = new JLabel("Mês:");
		lblMes.setDisplayedMnemonic('M');
		lblMes.setDisplayedMnemonic(KeyEvent.VK_M);
		lblMes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMes.setBounds(40, 29, 32, 16);
		orcamentoPanel.add(lblMes);
		
		JLabel lblCategoria = new JLabel("Categoria:");
		lblCategoria.setDisplayedMnemonic('C');
		lblCategoria.setDisplayedMnemonic(KeyEvent.VK_C);
		lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCategoria.setBounds(184, 29, 60, 16);
		orcamentoPanel.add(lblCategoria);
		
		JPanel TabelaPanel = new JPanel();
		TabelaPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		TabelaPanel.setBackground(new Color(255, 255, 255));
		TabelaPanel.setBounds(26, 70, 604, 217);
		orcamentoPanel.add(TabelaPanel);
		TabelaPanel.setLayout(new BorderLayout(0,0));
		
        DefaultTableModel tableModel = new DefaultTableModel(colunasTDespesa, 12) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

		tableOrcamento = new JTable(tableModel){
		    @Override
		    public boolean editCellAt(int row, int column, EventObject e) {
		        boolean result = super.editCellAt(row, column, e);

		        if (getCellEditor() instanceof DefaultCellEditor && getColumnClass(column) == Boolean.class) {
		            SwingUtilities.invokeLater(() -> {
		                if (editorComp != null) {
		                    editorComp.requestFocusInWindow();
		                    ((JCheckBox) editorComp).setSelected(!((JCheckBox) editorComp).isSelected());
		                }
		            });
		        }

		        return result;
		    }
		};
		
		TableCellEditor checkBoxEditor = new DefaultCellEditor(new JCheckBox()) {
		    @Override
		    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		        JCheckBox checkBox = (JCheckBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
		        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		        return checkBox;
		    }
		};

		int lastColumn = tableOrcamento.getColumnCount() - 1;
		tableOrcamento.getColumnModel().getColumn(lastColumn).setCellEditor(checkBoxEditor);
		
        TableCellEditor cellEditor = new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                boolean result = super.stopCellEditing();
                if (tableOrcamento.isEditing()) {
                	tableOrcamento.getCellEditor().stopCellEditing();
                }
                return result;
            }
        };
        tableOrcamento.setDefaultEditor(Object.class, cellEditor);        
		tableOrcamento.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableOrcamento.setSelectionBackground(SystemColor.inactiveCaption);
		tableOrcamento.setShowVerticalLines(true);
		tableOrcamento.setSurrendersFocusOnKeystroke(true);
		tableOrcamento.getColumnModel().getColumn(0).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(0).setPreferredWidth(109);
		tableOrcamento.getColumnModel().getColumn(1).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(1).setPreferredWidth(53);
		tableOrcamento.getColumnModel().getColumn(2).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(2).setPreferredWidth(87);
		tableOrcamento.getColumnModel().getColumn(3).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(3).setPreferredWidth(231);
		tableOrcamento.getColumnModel().getColumn(4).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(4).setPreferredWidth(80);
		tableOrcamento.getColumnModel().getColumn(5).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(5).setPreferredWidth(73);
		
		List<TabelaOrcamento> lista = new ArrayList<TabelaOrcamento>();
		OrcamentoDaoJDBC orcamentoDao = new OrcamentoDaoJDBC(conn);		
		lista = orcamentoDao.buscaCompletaPorMes(mes);	
		
		tableModel.setRowCount(0); 
		tableOrcamento.getColumnModel().getColumn(5).setCellRenderer(tableOrcamento.getDefaultRenderer(Boolean.class));
		
		for (TabelaOrcamento tab : lista) {
		    Object[] rowData = {tab.formatarData(tab.getData()), tab.getDia(), tab.getTipo(), tab.getDescricao(), formatador.format(tab.getValor()), tab.isPaga()};
		    tableModel.addRow(rowData);
		}
		tableOrcamento.setShowHorizontalLines(true);
		tableOrcamento.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
				
        JScrollPane scrollPane = new JScrollPane(tableOrcamento);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
        TabelaPanel.add(scrollPane, BorderLayout.CENTER);
        
		JButton btnPesquisarDespesa = new JButton("Pesquisar Despesa");
		btnPesquisarDespesa.setAutoscrolls(true);
		btnPesquisarDespesa.setMnemonic('P');
		btnPesquisarDespesa.setMnemonic(KeyEvent.VK_P);
		btnPesquisarDespesa.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnPesquisarDespesa.setBackground(Color.WHITE);
		btnPesquisarDespesa.setBounds(26, 305, 146, 26);
		orcamentoPanel.add(btnPesquisarDespesa);
		
		JButton btnGraficoBarras = new JButton("Gráfico em Barras");
		btnGraficoBarras.setMnemonic('B');
		btnGraficoBarras.setMnemonic(KeyEvent.VK_B);
		btnGraficoBarras.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGraficoBarras.setBackground(Color.WHITE);
		btnGraficoBarras.setBounds(184, 305, 133, 26);
		orcamentoPanel.add(btnGraficoBarras);
		
		JButton btnGraficoPizza = new JButton("Gráfico em Pizza");
		btnGraficoPizza.setMnemonic('G');
		btnGraficoPizza.setMnemonic(KeyEvent.VK_G);
		btnGraficoPizza.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGraficoPizza.setBackground(Color.WHITE);
		btnGraficoPizza.setBounds(324, 305, 139, 26);
		orcamentoPanel.add(btnGraficoPizza);		
		
		JPanel InformativoPanel = new JPanel();
		InformativoPanel.setBackground(new Color(255, 255, 255));
		InformativoPanel.setBounds(16, 6, 1163, 78);
		getContentPane().add(InformativoPanel);
		InformativoPanel.setLayout(null);
		
		JLabel lblReceitas = new JLabel("Receitas");
		lblReceitas.setHorizontalAlignment(SwingConstants.CENTER);
		lblReceitas.setHorizontalTextPosition(SwingConstants.CENTER);
		lblReceitas.setForeground(new Color(0, 0, 139));
		lblReceitas.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblReceitas.setBounds(46, 8, 118, 26);
		InformativoPanel.add(lblReceitas);
		
		JLabel lblDespesas = new JLabel("Despesas");
		lblDespesas.setHorizontalTextPosition(SwingConstants.CENTER);
		lblDespesas.setHorizontalAlignment(SwingConstants.CENTER);
		lblDespesas.setForeground(new Color(0, 0, 139));
		lblDespesas.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblDespesas.setBounds(185, 8, 147, 26);
		InformativoPanel.add(lblDespesas);
		
		JLabel lblSaldo = new JLabel("Saldo");
		lblSaldo.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaldo.setForeground(new Color(0, 0, 139));
		lblSaldo.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblSaldo.setBounds(344, 8, 167, 26);
		InformativoPanel.add(lblSaldo);
		
		JLabel lblTotalPago = new JLabel("Total Pago");
		lblTotalPago.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPago.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPago.setForeground(new Color(0, 0, 139));
		lblTotalPago.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTotalPago.setBounds(544, 8, 190, 26);
		InformativoPanel.add(lblTotalPago);
		
		JLabel lblTotalPagar = new JLabel("Total A Pagar");
		lblTotalPagar.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPagar.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPagar.setForeground(new Color(0, 0, 139));
		lblTotalPagar.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTotalPagar.setBounds(763, 8, 157, 26);
		InformativoPanel.add(lblTotalPagar);
		
		JLabel lblInvestimentos = new JLabel("Investimentos");
		lblInvestimentos.setHorizontalTextPosition(SwingConstants.CENTER);
		lblInvestimentos.setHorizontalAlignment(SwingConstants.CENTER);
		lblInvestimentos.setForeground(new Color(0, 0, 139));
		lblInvestimentos.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblInvestimentos.setBounds(962, 8, 157, 26);
		InformativoPanel.add(lblInvestimentos);
		
		RendaMensalDaoJDBC rendaMesalDao = new RendaMensalDaoJDBC(conn);
		JLabel lblReceitasValor = new JLabel(String.format("R$%s", formatador.format(rendaMesalDao.valorReceitaTotalPorMes(mes, ano))));
		lblReceitasValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblReceitasValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblReceitasValor.setForeground(new Color(0, 0, 0));
		lblReceitasValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblReceitasValor.setBounds(46, 46, 118, 26);
		InformativoPanel.add(lblReceitasValor);
		
		JLabel lblDespesasValor = new JLabel(String.format("R$%s", formatador.format(orcamentoDao.valorDespesaTotalPorMes(mes, ano))));
		lblDespesasValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblDespesasValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblDespesasValor.setForeground(Color.BLACK);
		lblDespesasValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDespesasValor.setBounds(185, 46, 147, 26);
		InformativoPanel.add(lblDespesasValor);
		
		double saldo = rendaMesalDao.valorReceitaTotalPorMes(mes, ano) - orcamentoDao.valorDespesaTotalPorMes(mes, ano);
		JLabel lblSaldoValor = new JLabel(String.format("R$%s", formatador.format(saldo)));
		lblSaldoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSaldoValor.setHorizontalAlignment(SwingConstants.CENTER);
		if(saldo > 0)
			lblSaldoValor.setForeground(Color.BLACK);
		else
			lblSaldoValor.setForeground(Color.RED);
		lblSaldoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblSaldoValor.setBounds(354, 46, 157, 26);
		InformativoPanel.add(lblSaldoValor);
		
		JLabel lblTotalPagoValor = new JLabel(String.format("R$%s", formatador.format(orcamentoDao.totalPago(mes, ano))));
		lblTotalPagoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPagoValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPagoValor.setForeground(Color.BLACK);
		lblTotalPagoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalPagoValor.setBounds(544, 46, 190, 26);
		InformativoPanel.add(lblTotalPagoValor);
		
		JLabel lblTotalPagarValor = new JLabel(String.format("R$%s", formatador.format(orcamentoDao.totalAPagar(mes, ano))));
		lblTotalPagarValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPagarValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPagarValor.setForeground(Color.BLACK);
		lblTotalPagarValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalPagarValor.setBounds(763, 46, 157, 26);
		InformativoPanel.add(lblTotalPagarValor);
		
		InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);
		String numeroFormatado = formatador.format(investimentoDao.totalAcumulado());
		JLabel lblInvestimentosValor = new JLabel(String.format("R$%s", numeroFormatado));
		lblInvestimentosValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblInvestimentosValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblInvestimentosValor.setForeground(Color.BLACK);
		lblInvestimentosValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblInvestimentosValor.setBounds(962, 46, 157, 26);
		InformativoPanel.add(lblInvestimentosValor);
		
		JPanel graficoPanel = new JPanel();
		graficoPanel.setBackground(new Color(255, 255, 255));
		graficoPanel.setBounds(642,24,499,307);
		graficoPanel.setLayout(null);
		orcamentoPanel.add(graficoPanel);
		
		ChartPanel chartGrafico = new ChartPanel(geraGraficoPizza(orcamentoDao.buscaCategoriasPorMes(mes), orcamentoDao.valorDespesaTotalPorMes(mes, ano)));
		chartGrafico.setMouseZoomable(false);
		chartGrafico.setBounds(0, 0, 497, 301);
		chartGrafico.setBackground(new Color(255, 255, 255));
		graficoPanel.add(chartGrafico);
		chartGrafico.setBorder(null);
		chartGrafico.setLayout(null);
		
		JComboBox<String> comboBoxMes = new JComboBox<String>();
		lblMes.setLabelFor(comboBoxMes);
		JComboBox<String> comboBoxCategoria = new JComboBox<String>();
		lblCategoria.setLabelFor(comboBoxCategoria);
		comboBoxMes.setMaximumRowCount(12);
		comboBoxMes.setModel(new DefaultComboBoxModel<String>(new String[] {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"}));
		comboBoxMes.setSelectedIndex(mes-1);
		comboBoxMes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				 if (e.getStateChange() == ItemEvent.SELECTED) {
					int mes = comboBoxMes.getSelectedIndex() + 1;
					Calendar calendario = Calendar.getInstance();
					int ano = calendario.get(Calendar.YEAR);
					
					comboBoxCategoria.setSelectedIndex(0);
					
					lblReceitasValor.setText(String.format("R$%.2f", rendaMesalDao.valorReceitaTotalPorMes(mes, ano)));
						
					lblDespesasValor.setText(String.format("R$%.2f", orcamentoDao.valorDespesaTotalPorMes(mes, ano)));
						
					double saldo = rendaMesalDao.valorReceitaTotalPorMes(mes, ano) - orcamentoDao.valorDespesaTotalPorMes(mes, ano);
					lblSaldoValor.setText(String.format("R$%.2f", saldo));
						
					lblTotalPagoValor.setText(String.format("R$%.2f", orcamentoDao.totalPago(mes, ano)));
						
					lblTotalPagarValor.setText(String.format("R$%.2f", orcamentoDao.totalAPagar(mes, ano)));
						
					InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);
					String numeroFormatado = formatador.format(investimentoDao.totalAcumulado());
					lblInvestimentosValor.setText(String.format("R$%s", numeroFormatado));		
					
					
					chartGrafico.setChart(geraGraficoPizza(orcamentoDao.buscaCategoriasPorMes(mes), rendaMesalDao.valorReceitaTotalPorMes(mes, ano)));
					
					DefaultTableModel model = (DefaultTableModel) tableOrcamento.getModel();

					model.setRowCount(0);
					tableOrcamento.getColumnModel().getColumn(5).setCellRenderer(tableOrcamento.getDefaultRenderer(Boolean.class));
					
					List<TabelaOrcamento> lista = new ArrayList<TabelaOrcamento>();				
					lista = orcamentoDao.buscaCompletaPorMes(mes);
					for (TabelaOrcamento tab : lista) {
					    Object[] rowData = {tab.formatarData(tab.getData()), tab.getDia(), tab.getTipo(), tab.getDescricao(), formatador.format(tab.getValor()), tab.isPaga()};
					    model.addRow(rowData);
					}

					tableOrcamento.repaint();	
		        }
			}
		});
		comboBoxMes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxMes.setBackground(new Color(255, 255, 255));
		comboBoxMes.setBounds(73, 24, 99, 26);
		orcamentoPanel.add(comboBoxMes);
		
		
		CategoriaDaoJDBC categoriaDao = new CategoriaDaoJDBC(conn);
		List<String> listaDescricoes = categoriaDao.listaDescricoes();
		String[] categorias = listaDescricoes.toArray(new String[listaDescricoes.size()]);
		comboBoxCategoria.setMaximumRowCount(categorias.length);
		comboBoxCategoria.setModel(new DefaultComboBoxModel<String>(categorias));
		comboBoxCategoria.setSelectedIndex(0);
		comboBoxCategoria.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String categoria = (String) comboBoxCategoria.getSelectedItem();
					List<TabelaOrcamento> lista = new ArrayList<TabelaOrcamento>();
					int mes = comboBoxMes.getSelectedIndex() + 1;

					if(categoria != "Todas") {
						Categoria cat = new Categoria();
						cat = categoriaDao.buscaPorDescricao(categoria);
						int idCategoria = cat.getCodigo();
						Calendar calendario = Calendar.getInstance();
						int ano = calendario.get(Calendar.YEAR);
						
						lista = orcamentoDao.buscaPorCategoriaEmes(idCategoria, mes, ano);
					}else 
						lista = orcamentoDao.buscaCompletaPorMes(mes);
					
					DefaultTableModel model = (DefaultTableModel) tableOrcamento.getModel();

					model.setRowCount(0);
					tableOrcamento.getColumnModel().getColumn(5).setCellRenderer(tableOrcamento.getDefaultRenderer(Boolean.class));
					
					for (TabelaOrcamento tab : lista) {
					    Object[] rowData = {tab.formatarData(tab.getData()), tab.getDia(), tab.getTipo(), tab.getDescricao(), formatador.format(tab.getValor()), tab.isPaga()};
					    model.addRow(rowData);
					}

					if(categoria != "Todas") {
						int quantidadeLinhas = 10;
					    for (int i = 0; i < quantidadeLinhas; i++) {
					        model.addRow(new Object[]{});
					    }
					}
			        
					tableOrcamento.repaint();	
				}
			}
		});
		comboBoxCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxCategoria.setBackground(Color.WHITE);
		comboBoxCategoria.setBounds(247, 24, 99, 26);
		orcamentoPanel.add(comboBoxCategoria);
		
		tableOrcamento.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        int row = tableOrcamento.getSelectedRow();
		        int column = tableOrcamento.getSelectedColumn();

		        if ((e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER) && column == tableOrcamento.getColumnCount() - 1) {
		            TableCellEditor cellEditor = tableOrcamento.getCellEditor(row, column);
		            if (cellEditor != null) 
		                cellEditor.stopCellEditing();
		            
		            String categoria = (String) comboBoxCategoria.getSelectedItem();
		            int mesSelecionado = comboBoxMes.getSelectedIndex() + 1;
		            if (categoria != "Todas") {
		            	Object[] rowData = new Object[tableModel.getColumnCount()];
		                for (int i = 0; i < tableModel.getColumnCount(); i++) {
		                    rowData[i] = tableModel.getValueAt(row, i);
		                }
		                
		                if (!isRowEmpty(rowData)) {
		                	String[] rowDataAsString = new String[tableModel.getColumnCount()];
		                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
		                        rowDataAsString[i] = String.valueOf(tableModel.getValueAt(row, i));
		                    }		                    
		                    updateRowInDatabase(conn, rowDataAsString, categoria, mes);
		                    chartGrafico.setChart(geraGraficoPizza(orcamentoDao.buscaCategoriasPorMes(mes), rendaMesalDao.valorReceitaTotalPorMes(mes, ano)));
		                    tableModel.fireTableDataChanged();
		                    
		                } else {
				            List<String> dados = new ArrayList<>();
				            
				            dados.add(rowData[0].toString());
				            dados.add(rowData[1].toString()+"/0"+mesSelecionado);
				            dados.add(rowData[2].toString());
				            dados.add(rowData[3].toString());
				            dados.add(categoria);
				            dados.add(rowData[4].toString());
				            
				            if(rowData.length == 6) {
				            	dados.add("Paga");
				            }else {
				            	dados.add("");
				            }
				            
				            InsereAlteraOrcamentoPelaTabela.importarDadosDespesa(conn, dados);
				            chartGrafico.setChart(geraGraficoPizza(orcamentoDao.buscaCategoriasPorMes(mes), rendaMesalDao.valorReceitaTotalPorMes(mes, ano)));	
				            tableModel.fireTableDataChanged();
		                }
		            } else {
		                InputOutput.showInfo("Para fazer alguma alteração é necessário selecionar uma categoria.", "Alterar linha");
		            }
		        }
		    }
		});
		
		btnImportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selecionarArquivos(conn);
			}
		});
		
		btnInvestimentos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new IgInvestimentos(conn);
			}
		});
		
		btnPesquisarDespesa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new IgPesquisarDespesa(conn, tableOrcamento);
			}
		});
		
		btnGraficoPizza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int mes = comboBoxMes.getSelectedIndex() + 1;
				chartGrafico.setChart(geraGraficoPizza(orcamentoDao.buscaCategoriasPorMes(mes), rendaMesalDao.valorReceitaTotalPorMes(mes, ano)));
			}
		});
		
		btnGraficoBarras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				int mes = comboBoxMes.getSelectedIndex() + 1;
				chartGrafico.setChart(geraGraficoBarra(orcamentoDao.buscaCategoriasPorMes(mes), rendaMesalDao.valorReceitaTotalPorMes(mes, ano)));				
			}
		});
				
		setResizable(false);
		getContentPane().setLayout(null);
		setVisible(true);
	}
	
	protected void selecionarArquivos(Connection conn) {
		JFileChooser file = new JFileChooser();
	    file.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    file.setMultiSelectionEnabled(true);
	    
	    FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("Arquivos CSV (*.csv)", "csv");
	    file.setFileFilter(csvFilter);
	    
	    int i = file.showSaveDialog(null);
	    
	    if(i == JFileChooser.APPROVE_OPTION) {
	    	File[] files;
	    	files = file.getSelectedFiles();
	    	importarArquivos(conn, files);
	    }
	}//selecionarArquivos()
	
	
	//1 para arquivo de receita, 2 para despesa, 3 para investimento
	protected void importarArquivos(Connection conn, File[] files) {
		Arquivo arq = new Arquivo();
				
		for(File f : files) {
			List<Line> linhasArquivo = arq.importar(f.getAbsolutePath());
			
			if (f.getName().endsWith(".csv")) {
				int tipo = arq.definirTipoArquivo(linhasArquivo);
				
				if(tipo == 1) {
					ArquivoReceita arqReceita = new ArquivoReceita(); 
					arqReceita.importarDadosReceita(conn, linhasArquivo);
				}
				if(tipo == 2) {
					ArquivoDespesa arqDespesa = new ArquivoDespesa(); 
					arqDespesa.importarDadosDespesa(conn, linhasArquivo);
				}
				if(tipo == 3) {
					ArquivoInvestimento arqInvestimento = new ArquivoInvestimento(); 
					arqInvestimento.importarDadosInvestimento(conn, linhasArquivo);
				}
	        } else {
	            InputOutput.showError("O arquivo não é um arquivo CSV.", "Importação do arquivo");
	        }
		}
	}
	
	private void updateRowInDatabase(Connection conn, String[] newRowData, String categoria, int mes) {
		List<String> dadosNovos = new ArrayList<>();
			
		dadosNovos.add(newRowData[0].toString());
		dadosNovos.add(newRowData[1].toString()+"/0"+mes);
		dadosNovos.add(newRowData[2].toString());
		dadosNovos.add(newRowData[3].toString());
		dadosNovos.add(categoria);
		dadosNovos.add(newRowData[4].toString());
		
		if(newRowData[5] == "true") {
			dadosNovos.add("Paga");
        }else {
        	dadosNovos.add("");
        }
		
		InsereAlteraOrcamentoPelaTabela.alterarDadosInvestimento(conn, dadosNovos);
	}
	
	protected String formatarDouble(double valor) {
	    DecimalFormat formato = new DecimalFormat("###,##0.00");
	    return formato.format(valor);
	}
	
	protected String formatarNumero(Double valor) {
		DecimalFormat formato = new DecimalFormat("0,000.00");
		String numeroFormatado = formato.format(valor);
		if(numeroFormatado.contains(".") || numeroFormatado.contains(",")) {
			if(numeroFormatado.contains(".")) 
				numeroFormatado = numeroFormatado.replace(".", "").replace(",", ".");
			else
				numeroFormatado = numeroFormatado.replace(",", ".");
		}
		return numeroFormatado;
	}
	
	protected boolean isRowEmpty(Object[] rowData) {
	    for (Object value : rowData) {
	        if (value != null && !value.toString().isEmpty()) {
	            return false;
	        }
	    }
	    return true;
	}
	
	protected JFreeChart geraGraficoPizza(Map<String, Double> categorias, double receitaMes) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		double porcentagem;
		
		for(String key : categorias.keySet()) {
			porcentagem = (categorias.get(key)*100)/receitaMes;
			dataset.setValue(key, porcentagem);
		}
		
		JFreeChart chart = ChartFactory.createPieChart("", dataset, true, false, false);
		
		PiePlot plot = (PiePlot) chart.getPlot();

		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
	    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"), new DecimalFormat("0%")));
	    plot.setLegendItemShape(new Ellipse2D.Double(-4, -4, 8, 8));
	    chart.getLegend().setPosition(RectangleEdge.LEFT);
	    plot.setBackgroundPaint(Color.WHITE);
	    plot.setInteriorGap(0.04);
        	    
        chart.getPlot().setOutlineVisible(false);
        chart.getPlot().setOutlinePaint(null);
		
		return chart;
	}
	
	protected JFreeChart geraGraficoBarra(Map<String, Double> categorias, double receitaMes) {
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    double porcentagem;
	    
	    for(String key : categorias.keySet()) {
			porcentagem = (categorias.get(key)*100)/receitaMes;
			dataset.addValue(porcentagem, "", key);
		}
	
	    JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
	    
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                return getRandomColor();
            }
        };

        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);        
        plot.setRenderer(renderer);

        CategoryItemLabelGenerator labelGenerator = new StandardCategoryItemLabelGenerator("{2}%", new DecimalFormat("0.00"), new DecimalFormat("0%"));
        renderer.setDefaultItemLabelGenerator(labelGenerator);
        renderer.setDefaultItemLabelsVisible(true);
        
	    return chart;
	}

    private static Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }
  
}//class IgOrcamento

