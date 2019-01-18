package com.jagex.runescape;

import javax.swing.*;
import javax.swing.GroupLayout.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

public class GlobalConfig {
	public static int FlameFoo = 0;
	public static int FlameBar = 0;

	public static void openFrame() {
		JFrame frame = new JFrame("HelloWorldSwing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel jLabel1 = new JLabel("Flame Foo");
		SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 255, 10);
		JSpinner entry = new JSpinner(model);

		Container pane = frame.getContentPane();

		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		pane.add(jLabel1);
		pane.add(entry);

		entry.addChangeListener(e -> {
			GlobalConfig.FlameFoo = model.getNumber().intValue();
		});

		JLabel jLabel2 = new JLabel("Flame Bar");
		SpinnerNumberModel model2 = new SpinnerNumberModel(0, 0, 255, 10);
		JSpinner entry2 = new JSpinner(model2);

		pane.add(jLabel2);
		pane.add(entry2);

		entry2.addChangeListener(e -> {
			GlobalConfig.FlameBar = model2.getNumber().intValue();
		});

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void addTextChangeListener(JTextComponent text, ChangeListener changeListener) {
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
			if (d1 != null) {
                d1.removeDocumentListener(dl);
            }
			if (d2 != null) {
                d2.addDocumentListener(dl);
            }
			dl.changedUpdate(null);
		});
		Document d = text.getDocument();
		if (d != null) {
            d.addDocumentListener(dl);
        }
	}
}
