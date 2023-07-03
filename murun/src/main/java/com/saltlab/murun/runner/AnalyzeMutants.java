package com.saltlab.murun.runner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saltlab.murun.runner.Stub.SUBJECT;
import com.saltlab.murun.utils.Settings;
import com.saltlab.webmutator.MutationRecordEx;

public class AnalyzeMutants extends JFrame implements ActionListener {
	JRadioButton rb1, rb2, rb3, rb4;
	JCheckBox missingElements, wrongElements, wrongElementState, brokenLink, brokenEventHandling, unExpectedElements, wrongSize, wrongColor, wrongPosition; 
	JCheckBox wrongPage, missingInfo, permission, session, missingPhoto, codeError, cosmetic, mathCalc, blankPage, database, codeDump, authentication, error404, search, upload, css, language; 
	JButton navButton, submitButton;
	JLabel candidateLabel;
	JLabel positionLabel;
	JLabel responseLabel;
	JLabel navStatus;
	JLabel picLabel;
	

	AnalysisRecord record = null;
	private MutationRunResult runResult;

	public enum MutantClass {
		eq, noneq, stillborn, unknown
	}
	
	 	private JList jList;
	    private JList jListForCopy;
	    private JButton copyButton;
		private JList jList2, jList3;
		private JButton copyButton2, copyButton3;
		private JList jListForCopy2, jListForCopy3;
	    private static final String[] listItems = {
	    		 "none", "missingElements", "wrongElements", "wrongElementState", "brokenLink", "brokenEventHandling", "unExpectedElements", "wrongSize", "wrongColor", "wrongPosition"		
	    };
	    private static final String[] listItems2 = {
	    		 "none", "wrongPage", "missingInfo", "permission", "session", "missingPhoto", "codeError", "cosmetic", "mathCalc", "blankPage", "database", "codeDump", "authentication", "error404", "search", "upload", "css", "language"
	    };
	    
	    private static final String[] listItems3 = {
	    		"none", "find", "execute", "assert", "executeAssertImmediate", "executeAssertPropagated"
	    };
	    
	    /*public JListCopyDemo() {
	        super("JList Demo");
	        setLayout(new FlowLayout());

	        jList = new JList(listItems);
	        jList.setFixedCellHeight(15);
	        jList.setFixedCellWidth(100);
	        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        jList.setVisibleRowCount(4);
	        add(new JScrollPane(jList));
	        
	        copyButton = new JButton("Copy>>>");
	        
	        copyButton.addActionListener(new ActionListener() {
	            
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                jListForCopy.setListData(jList.getSelectedValues());
	            }
	        });
	        
	        add(copyButton);
	        jListForCopy = new JList();
	        jListForCopy.setFixedCellHeight(15);
	        jListForCopy.setFixedCellWidth(100);
	        jList.setVisibleRowCount(4);
	        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	        add(new JScrollPane(jListForCopy));
	    }*/

	AnalyzeMutants(MutationRunResult runResult, AnalysisRecord record) {

		this.record = record;
		this.runResult = runResult;

		MutationCandidateEx candidate = runResult.getCandidate();

		Rectangle rect = candidate.getOrigNodeProperties().getPosition();

		
		


//		JPanel 
		BufferedImage imageToShow = showImages(runResult);
		Dimension maxBounds = Toolkit.getDefaultToolkit().getScreenSize();
		Image imageToShow2 = imageToShow.getScaledInstance(maxBounds.width, imageToShow.getHeight()* maxBounds.width/imageToShow.getWidth(), Image.SCALE_SMOOTH);
		JPanel imagePanel = new JPanel(new FlowLayout());
		picLabel = new JLabel(new ImageIcon(imageToShow2));
//		picLabel.setBounds(0, 300, maxBounds.width, maxBounds.height-300);
		imagePanel.add(picLabel);
		imagePanel.setAutoscrolls(true);
		JScrollPane scrollPane = new JScrollPane(imagePanel);
		scrollPane.setPreferredSize(new Dimension( maxBounds.width,maxBounds.height-350 ));
		add(scrollPane);
		
		
		String name = candidate.toString();

		candidateLabel = new JLabel(name);
		candidateLabel.setBounds(20, 10, 1100, 30);
		add(candidateLabel);

		positionLabel = new JLabel("Position is - " + rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height);
		positionLabel.setBounds(20, 40, 500, 30);
		add(positionLabel);


		try {
			navStatus = new JLabel("TestFailed:" + runResult.isFailed() + " Mutation Data :" + candidate.getRecord().getData());
			navStatus.setBounds(320, 100, 300, 30);
			add(navStatus);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		 setLayout(new FlowLayout());
//		setLayout(new GridLayout());

		 JPanel responsePanel = new JPanel(new FlowLayout());
		 	/* Bug Category*/
	        jList = new JList(listItems);
	        jList.setFixedCellHeight(15);
	        jList.setFixedCellWidth(150);
	        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        jList.setVisibleRowCount(10);
	        responsePanel.add(new JScrollPane(jList));
	        
	        copyButton = new JButton("Bug Category>>>");
	        
	        copyButton.addActionListener(new ActionListener() {
	            
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	List<String> toReplace = new ArrayList<String>();
	            	
	            	for(int i=0; i<jListForCopy.getModel().getSize(); i++) {
            			String existing = String.valueOf(jListForCopy.getModel().getElementAt(i));
            			toReplace.add(existing);
            		}
	            	for(String selectedVal: (List<String>)jList.getSelectedValuesList()) {
	            		if(!toReplace.contains(selectedVal)) {
	            			toReplace.add(selectedVal);
	            		}
	            	}
	                jListForCopy.setListData(toReplace.toArray());
//	                record.setSelectedCategories(jList.getSelectedValuesList());
	            }
	        });
	        
	        
	        
	        responsePanel.add(copyButton);
	        jListForCopy = new JList();
	        jListForCopy.setFixedCellHeight(15);
	        jListForCopy.setFixedCellWidth(100);
	        jList.setVisibleRowCount(10);
	        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	        responsePanel.add(new JScrollPane(jListForCopy));

	        /* Bug Labels*/
	        jList2 = new JList(listItems2);
	        jList2.setFixedCellHeight(15);
	        jList2.setFixedCellWidth(100);
	        jList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        jList2.setVisibleRowCount(10);
	        responsePanel.add(new JScrollPane(jList2));
	        
	        copyButton2 = new JButton("Bug Labels>>>");
	        
	        copyButton2.addActionListener(new ActionListener() {
	            
				@Override
	            public void actionPerformed(ActionEvent e) {
					List<String> toReplace = new ArrayList<String>();
	            	
	            	for(int i=0; i<jListForCopy2.getModel().getSize(); i++) {
            			String existing = String.valueOf(jListForCopy2.getModel().getElementAt(i));
            			toReplace.add(existing);
            		}
	            	for(String selectedVal: (List<String>)jList2.getSelectedValuesList()) {
	            		if(!toReplace.contains(selectedVal)) {
	            			toReplace.add(selectedVal);
	            		}
	            	}
	                jListForCopy2.setListData(toReplace.toArray());
//	                record.setSelectedLabels(jList2.getSelectedValuesList());
	            }
	        });
	        
	        responsePanel.add(copyButton2);
	        jListForCopy2 = new JList();
	        jListForCopy2.setFixedCellHeight(15);
	        jListForCopy2.setFixedCellWidth(100);
	        jList2.setVisibleRowCount(10);
	        jList2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	        responsePanel.add(new JScrollPane(jListForCopy2));
	        
	        /**/
	        
	        /* Testing Required*/
	        jList3 = new JList(listItems3);
	        jList3.setFixedCellHeight(15);
	        jList3.setFixedCellWidth(150);
	        jList3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        jList3.setVisibleRowCount(10);
	        responsePanel.add(new JScrollPane(jList3));
	        
	        copyButton3 = new JButton("Testing Required>>>");
	        
	        copyButton3.addActionListener(new ActionListener() {
	            
				@Override
	            public void actionPerformed(ActionEvent e) {
					List<String> toReplace = new ArrayList<String>();
	            	
	            	for(int i=0; i<jListForCopy3.getModel().getSize(); i++) {
            			String existing = String.valueOf(jListForCopy3.getModel().getElementAt(i));
            			toReplace.add(existing);
            		}
	            	for(String selectedVal: (List<String>)jList3.getSelectedValuesList()) {
	            		if(!toReplace.contains(selectedVal)) {
	            			toReplace.add(selectedVal);
	            		}
	            	}
	                jListForCopy3.setListData(toReplace.toArray());
//	                record.setSelectedLabels(jList3.getSelectedValuesList());
	            }
	        });
	        
	        responsePanel.add(copyButton3);
	        jListForCopy3 = new JList();
	        jListForCopy3.setFixedCellHeight(15);
	        jListForCopy3.setFixedCellWidth(100);
//	        jList3.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	        responsePanel.add(new JScrollPane(jListForCopy3));
	        
	        add(responsePanel);
	        
	        /**/
			
	        JPanel finalPanel = new JPanel(new FlowLayout());

			/* Mutant Class */
			rb1 = new JRadioButton("Equivalent");
			rb1.setBounds(50, 150, 100, 30);
			rb2 = new JRadioButton("Non-Equivalent");
			rb2.setBounds(200, 150, 100, 30);
			rb3 = new JRadioButton("Still-born");
			rb3.setBounds(350, 150, 100, 30);
			rb4 = new JRadioButton("Unknown");
			rb4.setBounds(500, 150, 100, 30);
			finalPanel.add(rb1);
			finalPanel.add(rb2);
			finalPanel.add(rb3);
			finalPanel.add(rb4);
			
			

			ButtonGroup bg = new ButtonGroup();
			bg.add(rb1);
			bg.add(rb2);
			bg.add(rb3);
			bg.add(rb4);
			

			submitButton = new JButton("Submit Response");
			submitButton.setBounds(100, 200, 200, 30);
			submitButton.setBackground(Color.GREEN);
			submitButton.setOpaque(true);
			submitButton.addActionListener(this);
			finalPanel.add(submitButton);
			


			navButton = new JButton("Reset");
			navButton.setBounds(100, 100, 200, 30);
			navButton.addActionListener(new ActionListener() {
	            
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	List<String> toReplace = new ArrayList<String>();
	                jListForCopy.setListData(toReplace.toArray());
	                jListForCopy2.setListData(toReplace.toArray());
	                jListForCopy3.setListData(toReplace.toArray());
	                submitButton.setEnabled(true);
//	                record.setSelectedCategories(jList.getSelectedValuesList());
	            }
	        });
			finalPanel.add(navButton);
			
			
			responseLabel = new JLabel("Response Recorded :" + record.mutantClass);
			responseLabel.setBounds(320, 200, 500, 30);
			finalPanel.add(responseLabel);
			
			
			
			add(finalPanel);
			
			
	        
		setSize(maxBounds.width, maxBounds.height);
//		setLayout(null);
		setVisible(true);
	}

	public BufferedImage showImages(MutationRunResult runResult) {
		String runFolder = runResult.runFolder;

		for (String stateName : runResult.mutationRecordMap.keySet()) {
			try {
				MutationRecordEx mutRecords = runResult.mutationRecordMap.get(stateName).get(0);

				String testName = stateName.substring(0, stateName.lastIndexOf('_'));
				String lineNumber = stateName.substring(stateName.lastIndexOf('_') + 1, stateName.length());

				System.out.println();
				
				File origFile = Paths.get(Settings.traceDir, runResult.testSuiteName, testName, lineNumber + ".png").toFile();
				File mutatedFile = Paths.get(runFolder, runResult.testSuiteName, testName, lineNumber + ".png").toFile();
				
				if(!origFile.exists()) {
					System.out.println(origFile.getPath() + "does not exist");
				}
				
				if(!mutatedFile.exists()) {
					System.out.println(mutatedFile.getPath() + "does not exist");
				}
				
				BufferedImage orig = ImageIO.read(origFile);
				Rectangle nodeRect = runResult.getCandidate().getOrigNodeProperties().getPosition();
				Graphics2D g2d = orig.createGraphics();

				try {
					g2d.setColor(Color.RED);
					g2d.drawRect(nodeRect.x, nodeRect.y, nodeRect.width, nodeRect.height);
				}catch(Exception ex) {
					System.err.println("Cannot draw " + nodeRect);
				}
				BufferedImage mutated = ImageIO.read(mutatedFile);

//				Image origScaled = orig.getScaledInstance(600, 400, 0);
//				Image mutatedScaled = mutated.getScaledInstance(600, 400, 0);

				BufferedImage imageToShow = new BufferedImage(orig.getWidth()+mutated.getWidth(), Math.max(orig.getHeight(), mutated.getHeight()), orig.getType());

				g2d = imageToShow.createGraphics();

				g2d.drawImage(orig, 0, 0, null);
				g2d.drawImage(mutated, orig.getWidth()+20, 0, null);

				
				return imageToShow;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;

		/*
		 * // var subject = methodInfo.runFolder.split('/')[1];
		 * 
		 * mutationRecords = Object.keys(methods[rowId].mutationRecordMap); for
		 * (mutationRecord of mutationRecords){ testName = mutationRecord.substring(0,
		 * mutationRecord.lastIndexOf('_'));
		 * 
		 * lineNumber = mutationRecord.substring(mutationRecord.lastIndexOf('_')+1,
		 * mutationRecord.length);
		 * 
		 * limgSrc = traceFolder + '/' + testSuiteName + '/'+ testName + '/' +
		 * lineNumber + '.png'; rimgSrc = mutantFolder + '/' + testSuiteName + '/' +
		 * testName + '/' + lineNumber +'.png';
		 */
	}

	public void actionPerformed(ActionEvent e) {
		/*if (e.getSource().equals(navButton)) {
			JOptionPane.showMessageDialog(this, "Navigating to mutant state.");
			record.liveAnalysis = true;
			navButton.setEnabled(false);
			navStatus.setText("Status: Running Live Analysis");
			showImages(runResult);
		}*/

		if (e.getSource().equals(submitButton)) {
			MutantClass response = null;
			if (rb1.isSelected()) {
				response = MutantClass.eq;
			}
			if (rb2.isSelected()) {
				response = MutantClass.noneq;
			}
			if (rb3.isSelected()) {
				response = MutantClass.stillborn;
			}
			if (rb4.isSelected()) {
				response = MutantClass.unknown;
			}
			
			/* Get Bug Categories*/
			List<String> categoryList = new ArrayList<String>();
		    for (int i = 0; i < jListForCopy.getModel().getSize(); i++) {
	             categoryList.add( String.valueOf(jListForCopy.getModel().getElementAt(i)));
	        };
	        
	        record.selectedCategories =  categoryList;
	        
	        /* Get Bug Labels*/
			List<String> labelList = new ArrayList<String>();
		    for (int i = 0; i < jListForCopy2.getModel().getSize(); i++) {
	             labelList.add( String.valueOf(jListForCopy2.getModel().getElementAt(i)));
	        };
	        
	        record.selectedLabels =  labelList;
			
	        /* Get Bug Labels*/
			List<String> requiredList = new ArrayList<String>();
		    for (int i = 0; i < jListForCopy3.getModel().getSize(); i++) {
	             requiredList.add( String.valueOf(jListForCopy3.getModel().getElementAt(i)));
	        };
	        
	        record.reqTestList =  requiredList;
	        
	        if(response == null || categoryList.isEmpty() || labelList.isEmpty() || requiredList.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Error - Response missing.");
	        }
	        else {
				submitButton.setEnabled(false);
	        }
	        
			responseLabel.setText("response recorded - " + response);

			
			record.mutantClass = response;
			
			
		}
	}

	public static AnalysisRecord analyzeRunResult(MutationRunResult runResult, SUBJECT subject) {
		AnalysisRecord analysisRecord = new AnalysisRecord();
		analysisRecord.subject = subject;
		analysisRecord.candidate = runResult.getCandidate().toString();
		analysisRecord.killed = runResult.isFailed();
		AnalyzeMutants analysis = new AnalyzeMutants(runResult, analysisRecord);
		// analysis.
		analysis.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		Object lock = new Object();

		analysis.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				synchronized (lock) {
					// analysis.setVisible(false);
					analysis.dispose();
					lock.notify();
				}
			}

		});

		synchronized (lock) {
			while (analysis.isVisible())
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			System.out.println("Working now");
		}

		System.out.println(analysisRecord);
		return analysisRecord;
	}

	public static void main(String args[]) {
		SUBJECT subject = SUBJECT.collabtive;
		if(args.length==1) {
			subject = Stub.getSubject(args[0]);
		}
		String runFolder = "2";

		HashMap<String, MutationRunResult> runResults = MutationRunner.loadRunResult(subject, runFolder);
		// List<MutationCandidateEx> candidates =
		// MutationRunner.getCandidatesFromResult(runResults, false);
		// MutationRunResult runResult =
		// (MutationRunResult)runResults.values().toArray()[0];

		List<AnalysisRecord> analysisRecords = new ArrayList<>();
		int limit = 55, index = 0;
		for (MutationRunResult runResult : runResults.values()) {
			if (index >= limit)
				break;
			if (runResult.testSuiteName == null) {
				runResult.testSuiteName = subject.name();
			}
			try {
			if(runResult.getFailedTests().get(0).getLocation().equalsIgnoreCase("overall")) {
				System.out.println("Skipping run with overall failure");
				continue;
			}}catch(Exception ex) {
				
			}
			try {
				AnalysisRecord record = analyzeRunResult(runResult, subject);
				record.computeSeverityScore();
				record.computeStubbornScore();
				analysisRecords.add(record);
				index += 1;
			}catch(Exception ex) {
				System.out.println("Could not analyze record" + runResult);
			}
		}

		Gson gson = new Gson();

		File analysisFile = Paths.get(Settings.MUTATION_DIR, subject.name(), runFolder, Settings.MUTATION_REPORT_DIR,
				Settings.MUTANT_ANALYSIS_FILE).toFile();

		try {
			FileWriter writer = new FileWriter(analysisFile);
			writer.write(gson.toJson(analysisRecords));
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		File analysisBackup = Paths.get(Settings.MUTATION_DIR, subject.name(), runFolder, Settings.MUTATION_REPORT_DIR,
				Settings.MUTANT_ANALYSIS_FILE_BACKUP).toFile();
		try {
			List<AnalysisRecord> existing = null;
			if (analysisBackup.exists()) {
				java.lang.reflect.Type recordListType = new TypeToken<List<AnalysisRecord>>() {
				}.getType();
				existing = (List<AnalysisRecord>) gson.fromJson(new FileReader(analysisBackup), recordListType);
			} else {
				existing = new ArrayList<>();
			}
			existing.addAll(analysisRecords);
			FileWriter writer = new FileWriter(analysisBackup);
			writer.append(gson.toJson(existing));
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
