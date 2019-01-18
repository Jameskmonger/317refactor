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
		final JFrame frame = new JFrame("HelloWorldSwing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JLabel jLabel1 = new JLabel("Flame Foo");
		final SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 255, 10);
		final JSpinner entry = new JSpinner(model);

		final Container pane = frame.getContentPane();

		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		pane.add(jLabel1);
		pane.add(entry);

		entry.addChangeListener(e -> {
			GlobalConfig.FlameFoo = model.getNumber().intValue();
		});

		final JLabel jLabel2 = new JLabel("Flame Bar");
		final SpinnerNumberModel model2 = new SpinnerNumberModel(0, 0, 255, 10);
		final JSpinner entry2 = new JSpinner(model2);

		pane.add(jLabel2);
		pane.add(entry2);

		entry2.addChangeListener(e -> {
			GlobalConfig.FlameBar = model2.getNumber().intValue();
		});

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void addTextChangeListener(final JTextComponent text, final ChangeListener changeListener) {
		final DocumentListener dl = new DocumentListener() {
			private int lastChange = 0, lastNotifiedChange = 0;

			@Override
			public void insertUpdate(final DocumentEvent e) {
				this.changedUpdate(e);
			}

			@Override
			public void removeUpdate(final DocumentEvent e) {
				this.changedUpdate(e);
			}

			@Override
			public void changedUpdate(final DocumentEvent e) {
                this.lastChange++;
				SwingUtilities.invokeLater(() -> {
					if (this.lastNotifiedChange != this.lastChange) {
                        this.lastNotifiedChange = this.lastChange;
						changeListener.stateChanged(new ChangeEvent(text));
					}
				});
			}
		};
		text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
			final Document d1 = (Document)e.getOldValue();
			final Document d2 = (Document)e.getNewValue();
			if (d1 != null) {
                d1.removeDocumentListener(dl);
            }
			if (d2 != null) {
                d2.addDocumentListener(dl);
            }
			dl.changedUpdate(null);
		});
		final Document d = text.getDocument();
		if (d != null) {
            d.addDocumentListener(dl);
        }
	}
}
