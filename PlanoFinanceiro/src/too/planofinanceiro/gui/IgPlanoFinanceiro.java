package too.planofinanceiro.gui;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import mos.reader.Line;
import too.planofinanceiro.arquivo.Arquivo;
import too.planofinanceiro.arquivo.ArquivoDespesa;
import too.planofinanceiro.arquivo.ArquivoInvestimento;
import too.planofinanceiro.arquivo.ArquivoReceita;
import javax.swing.SwingConstants;

public class IgPlanoFinanceiro extends JFrame {

	public IgPlanoFinanceiro(Connection conn) {
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Planejamento Financeiro");
		setBounds(100, 100, 1207, 521);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JButton btnImportar = new JButton("Importar");
		btnImportar.setBackground(new Color(255, 255, 255));
		btnImportar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnImportar.setMnemonic('o');
		btnImportar.setMnemonic(KeyEvent.VK_O);
		btnImportar.setBounds(938, 444, 106, 26);
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
		orcamentoPanel.setBounds(16, 87, 1163, 345);
		getContentPane().add(orcamentoPanel);
		orcamentoPanel.setLayout(null);
		
		JLabel lblMes = new JLabel("Mês:");
		lblMes.setDisplayedMnemonic('M');
		lblMes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMes.setBounds(40, 29, 32, 16);
		orcamentoPanel.add(lblMes);
		
		Choice choiceMes = new Choice();
		choiceMes.setBounds(69, 29, 84, 21);
		orcamentoPanel.add(choiceMes);
		
		JLabel lblCategoria = new JLabel("Categoria:");
		lblCategoria.setDisplayedMnemonic('C');
		lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCategoria.setBounds(184, 29, 60, 16);
		orcamentoPanel.add(lblCategoria);
		
		Choice choiceCategoria = new Choice();
		choiceCategoria.setBounds(243, 29, 84, 21);
		orcamentoPanel.add(choiceCategoria);
		setVisible(true);
		
		
		btnImportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selecionarArquivos(conn);
			}
		});
		
		setResizable(false);
		getContentPane().setLayout(null);
		
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
		lblReceitas.setBounds(60, 8, 87, 26);
		InformativoPanel.add(lblReceitas);
		
		JLabel lblDespesas = new JLabel("Despesas");
		lblDespesas.setHorizontalTextPosition(SwingConstants.CENTER);
		lblDespesas.setHorizontalAlignment(SwingConstants.CENTER);
		lblDespesas.setForeground(new Color(0, 0, 139));
		lblDespesas.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblDespesas.setBounds(175, 8, 87, 26);
		InformativoPanel.add(lblDespesas);
		
		JLabel lblSaldo = new JLabel("Saldo");
		lblSaldo.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaldo.setForeground(new Color(0, 0, 139));
		lblSaldo.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblSaldo.setBounds(349, 8, 87, 26);
		InformativoPanel.add(lblSaldo);
		
		JLabel lblTotalPago = new JLabel("Total Pago");
		lblTotalPago.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTotalPago.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalPago.setForeground(new Color(0, 0, 139));
		lblTotalPago.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblTotalPago.setBounds(611, 8, 110, 26);
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
		lblInvestimentos.setBounds(1015, 8, 129, 26);
		InformativoPanel.add(lblInvestimentos);
	}
	
	protected void selecionarArquivos(Connection conn) {
		JFileChooser file = new JFileChooser();
	    file.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    file.setMultiSelectionEnabled(true);
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
