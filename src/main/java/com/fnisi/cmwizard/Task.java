package com.fnisi.cmwizard;

import javax.swing.*;
import java.io.File;

class Task extends SwingWorker<Void, Void> {
    private File file;

    public Task(File file) {
        this.file = file;
    }

    @Override
    protected Void doInBackground() throws Exception {
        XMLReader xmlReader = new XMLReader(file);
        TableCreator tc = new TableCreator(xmlReader);
        tc.createTabs();
        return null;
    }

    @Override
    protected void done() {
        System.out.println("DONE");
    }
}

