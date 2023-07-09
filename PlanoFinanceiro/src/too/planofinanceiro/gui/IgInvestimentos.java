package too.planofinanceiro.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Paint;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import too.planofinanceiro.dao.implementacao.InvestimentoDaoJDBC;
import too.planofinanceiro.entidades.Investimento;
import too.planofinanceiro.util.InsereAlteraInvestimentoPelaTabela;

public class IgInvestimentos extends JFrame {
	private JTable tableInvestimento;
	private List<String[]> editedRows = new ArrayList<>();
	
	public IgInvestimentos(Connection conn) {
		String[] colunasTInvestimento = {"Objetivo", "Estratégia", "Nome", "Valor Investido", "Posição", "Rendimento Bruto", "Rentabilidade", "Vencimento"};
		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');
        DecimalFormat formatador = new DecimalFormat("#,##0.00", simbolos);
		
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
		
		DefaultTableModel tableModel = new DefaultTableModel(colunasTInvestimento, 8) {
            boolean[] canEdit = new boolean[]{
                    true, true, true, true, true, false, false, true
            };

            @Override
            public boolean isCellEditable(int row, int column) {
                return canEdit[column];
            }
        };
        
        tableInvestimento = new JTable(tableModel);
        tableInvestimento.setFocusable(true);
        TableCellEditor cellEditor = new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                boolean result = super.stopCellEditing();
                if (tableInvestimento.isEditing()) {
                	tableInvestimento.getCellEditor().stopCellEditing();
                }
                return result;
            }
        };
        tableInvestimento.setDefaultEditor(Object.class, cellEditor);     
		tableInvestimento.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tableInvestimento.setSelectionBackground(SystemColor.inactiveCaption);
		tableInvestimento.setShowVerticalLines(true);
		tableInvestimento.setSurrendersFocusOnKeystroke(true);
		tableInvestimento.getColumnModel().getColumn(0).setPreferredWidth(105);
		tableInvestimento.getColumnModel().getColumn(0).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(1).setPreferredWidth(90);
		tableInvestimento.getColumnModel().getColumn(1).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(2).setPreferredWidth(105);
		tableInvestimento.getColumnModel().getColumn(2).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(3).setPreferredWidth(80);
		tableInvestimento.getColumnModel().getColumn(3).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(4).setPreferredWidth(80);
		tableInvestimento.getColumnModel().getColumn(4).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(5).setPreferredWidth(80);
		tableInvestimento.getColumnModel().getColumn(5).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(6).setPreferredWidth(80);
		tableInvestimento.getColumnModel().getColumn(6).setResizable(false);
		tableInvestimento.getColumnModel().getColumn(7).setPreferredWidth(90);
		tableInvestimento.getColumnModel().getColumn(7).setResizable(false);
		
		List<Investimento> lista = new ArrayList<Investimento>();
		InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);		
		lista = investimentoDao.buscaCompleta();	
		
		tableModel.setRowCount(0); 
		
		for (Investimento tab : lista) {
		    Object[] rowData = {tab.getObjetivo(), tab.getEstrategia(), tab.getNome(), 
		    		("R$" + formatarDouble(tab.getValorInvestido())), ("R$" +formatarDouble(tab.getPosicao())), 
		    		("R$" +formatarDouble(tab.getRendimentoBruto())), 
		    		String.format("%.2f%%", tab.getRentabilidade()), tab.formatarData(tab.getVencimento())};
		    tableModel.addRow(rowData);
		}
		int quantidadeLinhas = 10;
	    for (int i = 0; i < quantidadeLinhas; i++) {
	    	tableModel.addRow(new Object[]{});
	    }
		TabelaPanel.setLayout(new BorderLayout(0, 0));
		tableInvestimento.setShowHorizontalLines(true);
		tableInvestimento.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
		
		JScrollPane scrollPane = new JScrollPane(tableInvestimento);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TabelaPanel.add(scrollPane);
		
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB && e.getID() == KeyEvent.KEY_PRESSED) {
                    int row = tableInvestimento.getSelectedRow();
                    int column = tableInvestimento.getSelectedColumn();
                    if (column == tableInvestimento.getColumnCount() - 1 && isRowEmpty(row)) {
                        TableCellEditor cellEditor = tableInvestimento.getCellEditor(row, column);
                        if (cellEditor != null) {
                            cellEditor.stopCellEditing();
                        }
                        acionarBtnTab(conn, row, column);
                    }
                }
                return false;
            }
        });
        
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            
            if (row >= 0 && row < editedRows.size() && column >= 0) {
                String[] oldRowData = editedRows.get(row);
                
                String[] newRowData = new String[tableModel.getColumnCount()];
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    newRowData[i] = tableModel.getValueAt(row, i).toString();
                }
                
                updateRowInDatabase(conn, oldRowData, newRowData);
            }
        });
		
		JButton btnGraficoBarras = new JButton("Gráfico em Barras");
		btnGraficoBarras.setBounds(802, 439, 133, 26);
		getContentPane().add(btnGraficoBarras);
		btnGraficoBarras.setMnemonic('B');
		btnGraficoBarras.setMnemonic(KeyEvent.VK_B);
		btnGraficoBarras.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGraficoBarras.setBackground(Color.WHITE);
		
		JButton btnGraficoEmColunas = new JButton("Gráfico em Colunas");
		btnGraficoEmColunas.setBounds(947, 439, 139, 26);
		getContentPane().add(btnGraficoEmColunas);
		btnGraficoEmColunas.setMnemonic('c');
		btnGraficoEmColunas.setMnemonic(KeyEvent.VK_C);
		btnGraficoEmColunas.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGraficoEmColunas.setBackground(Color.WHITE);
		
		JButton btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
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
		
		JLabel lblTotalInvestidoValor = new JLabel(String.format("R$%s", formatador.format(investimentoDao.totalInvestido())));
		lblTotalInvestidoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalInvestidoValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalInvestidoValor.setForeground(Color.BLACK);
		lblTotalInvestidoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalInvestidoValor.setBounds(37, 44, 156, 26);
		getContentPane().add(lblTotalInvestidoValor);
		
		JLabel lblTotalAcumuladoValor = new JLabel(String.format("R$%s", formatador.format(investimentoDao.totalAcumulado())));
		lblTotalAcumuladoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalAcumuladoValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalAcumuladoValor.setForeground(Color.BLACK);
		lblTotalAcumuladoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalAcumuladoValor.setBounds(247, 44, 156, 26);
		getContentPane().add(lblTotalAcumuladoValor);
		
		JLabel lblRendimentoBrutoValor = new JLabel(String.format("R$%s", formatador.format(investimentoDao.rendimentoBruto())));
		lblRendimentoBrutoValor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblRendimentoBrutoValor.setHorizontalAlignment(SwingConstants.CENTER);
		lblRendimentoBrutoValor.setForeground(Color.BLACK);
		lblRendimentoBrutoValor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblRendimentoBrutoValor.setBounds(477, 44, 182, 26);
		getContentPane().add(lblRendimentoBrutoValor);
		
		JPanel graficoPanel = new JPanel();
		graficoPanel.setBackground(new Color(255, 255, 255));
		graficoPanel.setBounds(6, 18, 1167, 321);
		graficoPanel.setLayout(null);
		orcamentoPanel.add(graficoPanel);
		graficoPanel.setVisible(false);
				
		ChartPanel chartGrafico = new ChartPanel(geraGraficoBarra(lista, investimentoDao.rendimentoBruto()));
		chartGrafico.setMouseZoomable(false);
		chartGrafico.setBounds(0, 6, 1160, 316);
		chartGrafico.setBackground(new Color(255, 255, 255));
		graficoPanel.add(chartGrafico);
		chartGrafico.setBorder(null);
		chartGrafico.setLayout(null);
		
		tableInvestimento.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int row = tableInvestimento.rowAtPoint(e.getPoint());
		        int column = tableInvestimento.columnAtPoint(e.getPoint());
		        
		        if (row >= 0 && column >= 0) {
		            String[] rowData = new String[tableModel.getColumnCount()];
		            for (int i = 0; i < tableModel.getColumnCount(); i++) {
		                rowData[i] = tableModel.getValueAt(row, i).toString();
		            }
		            
		            editedRows.add(rowData);
		        }
		    }
		});
		
		ListSelectionModel selectionModel = tableInvestimento.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		            int row = tableInvestimento.getSelectedRow();
		            
		            if (row >= 0) {
		                String[] rowData = new String[tableModel.getColumnCount()];
		                for (int i = 0; i < tableModel.getColumnCount(); i++) {
		                    rowData[i] = tableModel.getValueAt(row, i).toString();
		                }
		                
		                editedRows.add(rowData);
		            }
		        }
		    }
		});
		
		btnGraficoBarras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TabelaPanel.setVisible(false);
				graficoPanel.setVisible(true);
				List<Investimento> lista = new ArrayList<Investimento>();
				InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);		
				lista = investimentoDao.buscaCompleta();
				chartGrafico.setChart(geraGraficoBarra(lista, investimentoDao.rendimentoBruto()));
			}
		});
		
		btnGraficoEmColunas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TabelaPanel.setVisible(false);
				graficoPanel.setVisible(true);
				List<Investimento> lista = new ArrayList<Investimento>();
				InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);		
				lista = investimentoDao.buscaCompleta();
				chartGrafico.setChart(geraGraficoColuna(lista, investimentoDao.rendimentoBruto()));
			}
		});
		
		setResizable(false);
		getContentPane().setLayout(null);
		setVisible(true);
	}//IgInvestimentos(Connection conn)
	
	protected void acionarBtnTab(Connection conn, int row, int column) {
	    DefaultTableModel tableModel = (DefaultTableModel) tableInvestimento.getModel();
	    Object[] rowData = new Object[tableModel.getColumnCount()];
	    for (int i = 0; i < tableModel.getColumnCount(); i++) {
	        rowData[i] = tableModel.getValueAt(row, i);
	    }
	
	    List<String> dados = new ArrayList<>();
	    
	    dados.add(rowData[0].toString());
	    dados.add(rowData[1].toString());
	    dados.add(rowData[2].toString());
	    dados.add(rowData[3].toString());
	    dados.add(rowData[4].toString());
	    dados.add(rendimentoBruto(rowData[3].toString(), rowData[4].toString()));
	    dados.add(rentabilidade(rowData[3].toString(), rowData[4].toString()));
	    dados.add(rowData[7].toString());
	     
	    InsereAlteraInvestimentoPelaTabela.importarDadosInvestimento(conn, dados);	
	
	    List<Investimento> lista = new ArrayList<Investimento>();
		InvestimentoDaoJDBC investimentoDao = new InvestimentoDaoJDBC(conn);		
		lista = investimentoDao.buscaCompleta();	
		
		tableModel.setRowCount(0); 
		
		for (Investimento tab : lista) {
		    Object[] rowDataTab = {tab.getObjetivo(), tab.getEstrategia(), tab.getNome(), 
						    		("R$" + formatarDouble(tab.getValorInvestido())),
						    		("R$" +formatarDouble(tab.getPosicao())), 
									("R$" +formatarDouble(tab.getRendimentoBruto())), 
									String.format("%.2f%%", tab.getRentabilidade()), 
									tab.formatarData(tab.getVencimento())};
		    tableModel.addRow(rowDataTab);
		}
		int quantidadeLinhas = 10;
	    for (int i = 0; i < quantidadeLinhas; i++) {
	    	tableModel.addRow(new Object[]{});
	    }
	
	    tableInvestimento.repaint();	
	    
	}
	
	private void updateRowInDatabase(Connection conn, String[] oldRowData, String[] newRowData) {
		List<String> dadosAntigos = Arrays.asList(oldRowData);
		List<String> dadosNovos = Arrays.asList(newRowData);
		
		dadosAntigos.set(3, dadosAntigos.get(3).replace("R$", ""));
		dadosAntigos.set(4, dadosAntigos.get(4).replace("R$", ""));
		dadosAntigos.set(5, dadosAntigos.get(5).replace("R$", ""));
		
		if(dadosNovos.get(3).contains("R$")) 
			dadosNovos.set(3, dadosNovos.get(3).replace("R$", ""));
		
		if(dadosNovos.get(4).contains("R$")) 
			dadosNovos.set(4, dadosNovos.get(4).replace("R$", ""));
		
		if(dadosNovos.get(5).contains("R$")) 
			dadosNovos.set(5, dadosNovos.get(5).replace("R$", ""));
		
		rendimentoBruto(formatarNumero(dadosNovos.get(3)), formatarNumero(dadosNovos.get(4)));
	    rentabilidade(formatarNumero(dadosNovos.get(3)), formatarNumero(dadosNovos.get(4)));
		
		InsereAlteraInvestimentoPelaTabela.alterarDadosInvestimento(conn, dadosAntigos, dadosNovos);
	}
	
	protected String rendimentoBruto(String valorInvestido, String posicao) {
		double investido = Double.parseDouble(valorInvestido);
		double pos= Double.parseDouble(posicao);
		double rendimentoBruto = pos - investido;
		
		return String.format("%.2f", rendimentoBruto);
	}
	
	protected String rentabilidade(String valorInvestido, String posicao) {
		double investido = Double.parseDouble(valorInvestido);
		double pos= Double.parseDouble(posicao);
		double rentabilidade = pos - investido/ investido;
		
		return String.format("%.2f", rentabilidade);
	}
	
	protected String formatarDouble(double valor) {
	    DecimalFormat formato = new DecimalFormat("###,##0.00");
	    return formato.format(valor);
	}
	
	protected boolean isRowEmpty(int row) {
	    for (int i = 0; i < tableInvestimento.getColumnCount(); i++) {
	        Object value = tableInvestimento.getValueAt(row, i);
	        if (value != null && !value.toString().isEmpty()) {
	            return false;
	        }
	    }
	    return true;
	}
	
	protected JFreeChart geraGraficoBarra(List<Investimento> lista, double rendimentoBrutoTotal) {
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    double porcentagem;
	    
	    for(Investimento tab : lista) {
	    	porcentagem = (tab.getRendimentoBruto()*100)/rendimentoBrutoTotal;
			dataset.addValue(porcentagem, "", tab.getNome());
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
	
	protected JFreeChart geraGraficoColuna(List<Investimento> lista, double rendimentoBrutoTotal) {
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    double porcentagem;
	    
	    for(Investimento tab : lista) {
	    	porcentagem = (tab.getRendimentoBruto()*100)/rendimentoBrutoTotal;
			dataset.addValue(porcentagem, "", tab.getNome());
		}
	
	    JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.HORIZONTAL, false, false, false);
	    
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
	
	protected String formatarNumero(String valor) {
		if(valor.contains(".") || valor.contains(",")) {
			if(valor.contains(".")) 
			 valor = valor.replace(".", "").replace(",", ".");
			else
				valor = valor.replace(",", ".");
		}
		return valor;
	}
	
	 private static Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }
}//class IgInvestimentos
