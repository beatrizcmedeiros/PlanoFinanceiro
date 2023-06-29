package too.planofinanceiro.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

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
		lblMes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMes.setBounds(40, 29, 32, 16);
		orcamentoPanel.add(lblMes);
		
		JLabel lblCategoria = new JLabel("Categoria:");
		lblCategoria.setDisplayedMnemonic('C');
		lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCategoria.setBounds(184, 29, 60, 16);
		orcamentoPanel.add(lblCategoria);
		
		JPanel TabelaPanel = new JPanel();
		TabelaPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		TabelaPanel.setBackground(new Color(255, 255, 255));
		TabelaPanel.setBounds(26, 70, 604, 217);
		orcamentoPanel.add(TabelaPanel);
		TabelaPanel.setLayout(new BorderLayout(0,0));
		
		tableOrcamento = new JTable();
		tableOrcamento.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableOrcamento.setSelectionBackground(SystemColor.inactiveCaption);
		tableOrcamento.setShowVerticalLines(true);
		tableOrcamento.setModel(new DefaultTableModel(colunasTDespesa, 12));
		tableOrcamento.getColumnModel().getColumn(0).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(0).setPreferredWidth(109);
		tableOrcamento.getColumnModel().getColumn(1).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(1).setPreferredWidth(53);
		tableOrcamento.getColumnModel().getColumn(2).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(2).setPreferredWidth(87);
		tableOrcamento.getColumnModel().getColumn(3).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(3).setPreferredWidth(231);
		tableOrcamento.getColumnModel().getColumn(4).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(4).setPreferredWidth(62);
		tableOrcamento.getColumnModel().getColumn(5).setResizable(false);
		tableOrcamento.getColumnModel().getColumn(5).setPreferredWidth(73);
		List<TabelaOrcamento> lista = new ArrayList<TabelaOrcamento>();
		OrcamentoDaoJDBC orcamentoDao = new OrcamentoDaoJDBC(conn);		
		lista = orcamentoDao.buscaCompletaPorMes();	
		DefaultTableModel tableModel = (DefaultTableModel) tableOrcamento.getModel();
		tableModel.setRowCount(0); 
		for (TabelaOrcamento tab : lista) {
		    Object[] rowData = {tab.getData(), tab.getDia(), tab.getTipo(), tab.getDescricao(), tab.getValor(), tab.isPaga()};
		    tableModel.addRow(rowData);
		}
		tableOrcamento.setShowHorizontalLines(true);
		
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
		lblDespesas.setBounds(185, 8, 118, 26);
		InformativoPanel.add(lblDespesas);
		
		JLabel lblSaldo = new JLabel("Saldo");
		lblSaldo.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaldo.setForeground(new Color(0, 0, 139));
		lblSaldo.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblSaldo.setBounds(393, 8, 118, 26);
		InformativoPanel.add(lblSaldo);
		
		JLabel lblTotalPago = new JLabel("Total Pago");
		lblTotalPago.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPago.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPago.setForeground(new Color(0, 0, 139));
		lblTotalPago.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTotalPago.setBounds(611, 8, 123, 26);
		InformativoPanel.add(lblTotalPago);
		
		JLabel lblTotalPagar = new JLabel("Total A Pagar");
		lblTotalPagar.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPagar.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPagar.setForeground(new Color(0, 0, 139));
		lblTotalPagar.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTotalPagar.setBounds(763, 8, 129, 26);
		InformativoPanel.add(lblTotalPagar);
		
		JLabel lblInvestimentos = new JLabel("Investimentos");
		lblInvestimentos.setHorizontalTextPosition(SwingConstants.CENTER);
		lblInvestimentos.setHorizontalAlignment(SwingConstants.CENTER);
		lblInvestimentos.setForeground(new Color(0, 0, 139));
		lblInvestimentos.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblInvestimentos.setBounds(962, 8, 157, 26);
		InformativoPanel.add(lblInvestimentos);
		
		RendaMensalDaoJDBC rendaMesalDao = new RendaMensalDaoJDBC(conn);
		JLabel lblReceitasValor = new JLabel(String.format("R$%.2f", rendaMesalDao.valorReceitaTotalPorMes(mes, ano)));
		lblReceitasValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblReceitasValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblReceitasValor.setForeground(new Color(0, 0, 0));
		lblReceitasValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblReceitasValor.setBounds(46, 46, 118, 26);
		InformativoPanel.add(lblReceitasValor);
		
		JLabel lblDespesasValor = new JLabel(String.format("R$%.2f", orcamentoDao.valorDespesaTotalPorMes(mes, ano)));
		lblDespesasValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblDespesasValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblDespesasValor.setForeground(Color.BLACK);
		lblDespesasValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDespesasValor.setBounds(185, 46, 118, 26);
		InformativoPanel.add(lblDespesasValor);
		
		double saldo = rendaMesalDao.valorReceitaTotalPorMes(mes, ano) - orcamentoDao.valorDespesaTotalPorMes(mes, ano);
		JLabel lblSaldoValor = new JLabel(String.format("R$%.2f", saldo));
		lblSaldoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSaldoValor.setHorizontalAlignment(SwingConstants.CENTER);
		if(saldo > 0)
			lblSaldoValor.setForeground(Color.BLACK);
		else
			lblSaldoValor.setForeground(Color.RED);
		lblSaldoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblSaldoValor.setBounds(393, 46, 118, 26);
		InformativoPanel.add(lblSaldoValor);
		
		JLabel lblTotalPagoValor = new JLabel(String.format("R$%.2f", orcamentoDao.totalPago(mes, ano)));
		lblTotalPagoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPagoValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPagoValor.setForeground(Color.BLACK);
		lblTotalPagoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalPagoValor.setBounds(611, 46, 123, 26);
		InformativoPanel.add(lblTotalPagoValor);
		
		JLabel lblTotalPagarValor = new JLabel(String.format("R$%.2f", orcamentoDao.totalAPagar(mes, ano)));
		lblTotalPagarValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPagarValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPagarValor.setForeground(Color.BLACK);
		lblTotalPagarValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalPagarValor.setBounds(763, 46, 129, 26);
		InformativoPanel.add(lblTotalPagarValor);
		
		InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);
		String numeroFormatado = formatador.format(investimentoDao.totalInvestimentos());
		JLabel lblInvestimentosValor = new JLabel(String.format("R$%s", numeroFormatado));
		lblInvestimentosValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblInvestimentosValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblInvestimentosValor.setForeground(Color.BLACK);
		lblInvestimentosValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblInvestimentosValor.setBounds(962, 46, 157, 26);
		InformativoPanel.add(lblInvestimentosValor);
		
		JComboBox<String> comboBoxMes = new JComboBox<String>();
		comboBoxMes.setMaximumRowCount(12);
		comboBoxMes.setModel(new DefaultComboBoxModel<String>(new String[] {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"}));
		comboBoxMes.setSelectedIndex(mes-1);
		comboBoxMes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				 if (e.getStateChange() == ItemEvent.SELECTED) {
					int mes = comboBoxMes.getSelectedIndex() + 1;
					Calendar calendario = Calendar.getInstance();
					int ano = calendario.get(Calendar.YEAR);
							           
							           
					lblReceitasValor.setText(String.format("R$%.2f", rendaMesalDao.valorReceitaTotalPorMes(mes, ano)));
						
					lblDespesasValor.setText(String.format("R$%.2f", orcamentoDao.valorDespesaTotalPorMes(mes, ano)));
						
					double saldo = rendaMesalDao.valorReceitaTotalPorMes(mes, ano) - orcamentoDao.valorDespesaTotalPorMes(mes, ano);
					lblSaldoValor.setText(String.format("R$%.2f", saldo));
						
					lblTotalPagoValor.setText(String.format("R$%.2f", orcamentoDao.totalPago(mes, ano)));
						
					lblTotalPagarValor.setText(String.format("R$%.2f", orcamentoDao.totalAPagar(mes, ano)));
						
					InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);
					String numeroFormatado = formatador.format(investimentoDao.totalInvestimentos());
					lblInvestimentosValor.setText(String.format("R$%s", numeroFormatado));		
		        }
			}
		});
		comboBoxMes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxMes.setBackground(new Color(255, 255, 255));
		comboBoxMes.setBounds(73, 24, 99, 26);
		orcamentoPanel.add(comboBoxMes);
		
		
		JComboBox<String> comboBoxCategoria = new JComboBox<String>();
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
					
					if(categoria != "Todas") {
						Categoria cat = new Categoria();
						cat = categoriaDao.buscaPorDescricao(categoria);
						int idCategoria = cat.getCodigo();
						int mes = comboBoxMes.getSelectedIndex() + 1;
						Calendar calendario = Calendar.getInstance();
						int ano = calendario.get(Calendar.YEAR);
						
						lista = orcamentoDao.buscaPorCategoriaEmes(idCategoria, mes, ano);
					}else 
						lista = orcamentoDao.buscaCompletaPorMes();
					
					DefaultTableModel model = (DefaultTableModel) tableOrcamento.getModel();

					model.setRowCount(0);

					for (TabelaOrcamento tab : lista) {
					    Object[] rowData = {tab.getData(), tab.getDia(), tab.getTipo(), tab.getDescricao(), tab.getValor(), tab.isPaga()};
					    model.addRow(rowData);
					}

					tableOrcamento.repaint();	
				}
			}
		});
		comboBoxCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxCategoria.setBackground(Color.WHITE);
		comboBoxCategoria.setBounds(247, 24, 99, 26);
		orcamentoPanel.add(comboBoxCategoria);
		
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
			
		}
	}
}//class IgOrcamento
