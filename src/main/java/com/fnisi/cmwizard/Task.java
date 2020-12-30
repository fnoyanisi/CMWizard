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
        int progress = 0, totalRecords;

        setProgress(0);
        CmXmlReader cmXmlReader = new CmXmlReader(file);
        totalRecords = cmXmlReader.getTotalNumberOfManagedObjects();

        //setProgress(1);
        TableCreator tc = new TableCreator(cmXmlReader);
        tc.setTask(this);
        tc.createTabs();
        return null;
    }

    @Override
    protected void done() {
        System.out.println("DONE");
    }

    public void updateProgress(int p) {
        setProgress(p);
    }
}

