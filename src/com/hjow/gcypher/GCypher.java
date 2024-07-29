package com.hjow.gcypher;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.hjow.gcypher.interfaces.Disposeable;
import com.hjow.gcypher.modules.CypherModule;
import com.hjow.gcypher.modules.ModuleLoader;

/**
 * 암/복호화 기능을 제공하는 UI 클래스입니다.
 * 
 * @author HJOW
 *
 */
public class GCypher {
    protected JFrame            frame;
    protected JSplitPane        splitPane;
    protected JToolBar          toolbar;
    protected JTextArea         before, after;
    protected JComboBox<String> cbModule;
    protected JPasswordField    pwField;
    protected JButton           btnAct;
    protected JMenuItem         menuAct;
    protected GFileCypher       fileCypher;
    
    protected transient Vector<Disposeable> disposeables = new Vector<Disposeable>();
    protected transient Properties properties = new Properties();
    
    /** GCypher 기본 생성자이자 유일한 생성자입니다. */
    public GCypher() {
        init();
    }
    /** UI를 초기화합니다. */
    public void init() {
        try {
            boolean found = false;
            for(LookAndFeelInfo infos : UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equalsIgnoreCase(infos.getName())) {
                    UIManager.setLookAndFeel(infos.getClassName());
                    found = true;
                }
            }
            if(!found) UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ex) { ex.printStackTrace(); }
        
        frame = new JFrame();
        frame.setSize(500, 300);
        frame.setTitle("GCypher");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		exit();
        	}
		});
        
        frame.setLayout(new BorderLayout());
        
        JPanel pnMain = new JPanel();
        frame.add(pnMain, BorderLayout.CENTER);
        
        pnMain.setLayout(new BorderLayout());
        
        JPanel pnCenter = new JPanel();
        pnMain.add(pnCenter, BorderLayout.CENTER);
        
        pnCenter.setLayout(new BorderLayout());
        
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        pnCenter.add(splitPane, BorderLayout.CENTER);
        
        before = new JTextArea();
        after  = new JTextArea();
        after.setEditable(false);
        
        splitPane.setTopComponent(new JScrollPane(before));
        splitPane.setBottomComponent(new JScrollPane(after));
        
        toolbar = new JToolBar();
        pnCenter.add(toolbar, BorderLayout.NORTH);
        
        Vector<String> moduleNames = new Vector<String>();
        moduleNames.addAll(ModuleLoader.getNames());
        cbModule = new JComboBox<String>(moduleNames);
        toolbar.add(cbModule);
        
        pwField = new JPasswordField(20);
        toolbar.add(pwField);
        
        btnAct = new JButton("Convert");
        toolbar.add(btnAct);
        
        ActionListener eventAct = new ActionListener() {   
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {   
                    @Override
                    public void run() {
                        act();
                    }
                });
            }
        };
        
        btnAct.addActionListener(eventAct);
        
        fileCypher = new GFileCypher(this);
        disposeables.add(fileCypher);
        
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuFile = new JMenu("File");
        
        menuAct = new JMenuItem("Convert");
        menuAct.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
        menuAct.addActionListener(eventAct);
        menuFile.add(menuAct);
        
        menuFile.addSeparator();
        
        JMenuItem menuFileConv = new JMenuItem("GCypher File Tool");
        menuFileConv.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
        menuFile.add(menuFileConv);
        menuFileConv.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						fileCypher.open();
					}
				});
			}
		});
        
        menuFile.addSeparator();
        
        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
        menuFile.add(menuExit);
        menuExit.addActionListener(new ActionListener() {   
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        
        menuBar.add(menuFile);
        frame.setJMenuBar(menuBar);
    }
    /** UI 상에서 동작 버튼이 클릭되었을 때 호출됩니다. */
    protected void act() {
    	after.setText("");
        String moduleName = (String) cbModule.getSelectedItem();
        CypherModule module = ModuleLoader.get(moduleName);
        
        String password;
        char[] pwInput = pwField.getPassword();
        password = new String(pwInput);
        
        btnAct.setEnabled(false);
        menuAct.setEnabled(false);
        before.setEditable(false);
        try {
            after.setText(module.convert(before.getText(), password, properties));
        } catch(Throwable t) {
            after.setText("[ERROR]\n" + t.getMessage());
        }
        before.setEditable(true);
        btnAct.setEnabled(true);
        menuAct.setEnabled(true);
    }
    /** UI를 열어 본격적으로 프로그램 사용을 시작합니다. */
    public void open() {
    	properties.putAll(ModuleLoader.loadPropResource("/bundled.properties"));
    	properties.putAll(ModuleLoader.loadPropResource("/config.properties"));
    	
        frame.setVisible(true);
        splitPane.setDividerLocation(0.5);
    }
    /** JFrame 객체 반환 */
    public JFrame getFrame() {
    	return frame;
    }
    /**  프로그램 종료 */
    public void exit() {
    	frame.setVisible(false);
    	for(Disposeable d : disposeables) {
    		d.dispose();
    	}
    	disposeables.clear();
    	System.exit(0);
    }
    public static void main(String[] args) { new GCypher().open(); }
}
