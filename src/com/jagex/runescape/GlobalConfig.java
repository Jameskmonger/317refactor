package com.jagex.runescape;

import javax.swing.*;
import javax.swing.GroupLayout.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

public class GlobalConfig {
	public static String WalkHereTest = "Foo";

	public static void openFrame() {
		JFrame frame = new JFrame("HelloWorldSwing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel jLabel1 = new JLabel("Walk Here Text");
		JTextField entry = new JTextField();

		Container pane = frame.getContentPane();

		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		pane.add(jLabel1);
		pane.add(entry);

		addChangeListener(entry, e -> GlobalConfig.WalkHereTest = entry.getText());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
		Objects.requireNonNull(text);
		Objects.requireNonNull(changeListener);
		DocumentListener dl = new DocumentListener() {
			private int lastChange = 0, lastNotifiedChange = 0;

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				lastChange++;
				SwingUtilities.invokeLater(() -> {
					if (lastNotifiedChange != lastChange) {
						lastNotifiedChange = lastChange;
						changeListener.stateChanged(new ChangeEvent(text));
					}
				});
			}
		};
		text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
			Document d1 = (Document)e.getOldValue();
			Document d2 = (Document)e.getNewValue();
			if (d1 != null) d1.removeDocumentListener(dl);
			if (d2 != null) d2.addDocumentListener(dl);
			dl.changedUpdate(null);
		});
		Document d = text.getDocument();
		if (d != null) d.addDocumentListener(dl);
	}
}
