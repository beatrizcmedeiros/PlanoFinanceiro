package too.planofinanceiro.gui;

import java.awt.Color;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JButton;

public class IgPesquisarDespesa extends JFrame {
	private JTextField textFieldItemDespesa;
	
	public IgPesquisarDespesa(Connection conn) {
		
		getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Pesquisar Despesa");
		setBounds(100, 100, 397, 183);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblItemDespesa = new JLabel("Item de despesa:");
		lblItemDespesa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemDespesa.setDisplayedMnemonic('I');
		lblItemDespesa.setDisplayedMnemonic(KeyEvent.VK_I);
		lblItemDespesa.setBounds(20, 16, 108, 14);
		getContentPane().add(lblItemDespesa);
		
		JLabel lblProcurar = new JLabel("Procurar por:");
		lblProcurar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblProcurar.setBounds(20, 53, 108, 14);
		getContentPane().add(lblProcurar);
		
		textFieldItemDespesa = new JTextField();
		textFieldItemDespesa.setBounds(134, 9, 235, 28);
		getContentPane().add(textFieldItemDespesa);
		textFieldItemDespesa.setColumns(10);
		
		JRadioButton rdbtnData = new JRadioButton("Data");
		rdbtnData.setBackground(new Color(255, 255, 255));
		rdbtnData.setMnemonic('D');
		rdbtnData.setMnemonic(KeyEvent.VK_D);
		rdbtnData.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rdbtnData.setBounds(134, 49, 68, 23);
		getContentPane().add(rdbtnData);
		
		JRadioButton rdbtnDescricao = new JRadioButton("Descrição");
		rdbtnDescricao.setMnemonic('e');
		rdbtnDescricao.setMnemonic(KeyEvent.VK_E);
		rdbtnDescricao.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rdbtnDescricao.setBackground(Color.WHITE);
		rdbtnDescricao.setBounds(202, 49, 89, 23);
		getContentPane().add(rdbtnDescricao);
		
		JRadioButton rdbtnValor = new JRadioButton("Valor");
		rdbtnValor.setMnemonic('V');
		rdbtnValor.setMnemonic(KeyEvent.VK_V);
		rdbtnValor.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rdbtnValor.setBackground(Color.WHITE);
		rdbtnValor.setBounds(301, 49, 68, 23);
		getContentPane().add(rdbtnValor);
		
		JButton btnProximaDespesa = new JButton("Próxima Despesa");
		btnProximaDespesa.setBackground(new Color(255, 255, 255));
		btnProximaDespesa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnProximaDespesa.setMnemonic('P');
		btnProximaDespesa.setMnemonic(KeyEvent.VK_P);
		btnProximaDespesa.setBounds(122, 108, 143, 25);
		getContentPane().add(btnProximaDespesa);
		
		JButton btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnFechar.setBackground(new Color(255, 255, 255));
		btnFechar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnFechar.setMnemonic('F');
		btnFechar.setMnemonic(KeyEvent.VK_F);
		btnFechar.setBounds(280, 108, 89, 25);
		getContentPane().add(btnFechar);
		
		setResizable(false);
		getContentPane().setLayout(null);
		setVisible(true);
	}
}
