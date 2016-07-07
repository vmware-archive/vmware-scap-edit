package com.g2inc.scap.editor.gui.splash;
/* ESCAPE Software Copyright 2010 G2, Inc. - All rights reserved.
*
* ESCAPE is open source software distributed under GNU General Public License Version 3.  ESCAPE is not in the public domain 
* and G2, Inc. holds its copyright.  Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:

* 1. Redistributions of ESCAPE source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the ESCAPE Software distribution. 
* 3. Neither the name of G2, Inc. nor the names of any contributors may be used to endorse or promote products derived from this software without specific prior written permission. 

* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
* INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
* IN NO EVENT SHALL G2, INC., THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
* OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
* OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.

* You should have received a copy of the GNU General Public License Version 3 along with this program. 
* If not, see http://www.gnu.org/licenses/ for a copy.
*/

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

public class SplashScreen extends JWindow
{
    private String filename = null;

    private void adjustSize()
    {
        Dimension screenSize =
          Toolkit.getDefaultToolkit().getScreenSize();

        setSize(screenSize.width / 3, screenSize.height / 3);

        setLocationRelativeTo(null);
    }

    private void addListeners()
    {
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent me)
            {
                setVisible(false);
                dispose();
            }
        });
    }

    public ImageIcon createImageIcon(String pathToImage, int sizeX, int sizeY)
    {
        ImageIcon ret = null;

        if(pathToImage != null)
        {
            try
            {
            	InputStream is = new FileInputStream(pathToImage);
            	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            	byte[] buffer = new byte[1024];
            	int bytesRead = 0;
            	while ((bytesRead = is.read(buffer)) != -1) {
            		baos.write(buffer, 0, bytesRead);
            	}
            	ret = new ImageIcon(baos.toByteArray());

                Image i = ret.getImage();

                Image ni = i.getScaledInstance(sizeX, sizeY, java.awt.Image.SCALE_SMOOTH);

                ret = new ImageIcon(ni);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return ret;
    }

    private void loadAndScaleImage()
    {
       ImageIcon splashImage = createImageIcon(filename, getSize().width, getSize().height);

       imageLabel.setIcon(splashImage);
    }

    private void initComponents2()
    {
        adjustSize();
        addListeners();
    
        if(filename != null)
        {
            loadAndScaleImage();
        }
    }

    /** Creates new form BeanForm */
    public SplashScreen()
    {
        initComponents();
        initComponents2();
    }

    /** Creates new form BeanForm */
    public SplashScreen(String imageFileName)
    {
        initComponents();

        filename = imageFileName;
        
        initComponents2();

        final int pause = 6000;
        final Runnable closerRunner = new Runnable()
            {
                public void run()
                {
                    setVisible(false);
                    dispose();
                }
            };
            Runnable waitRunner = new Runnable()
            {
                public void run()
                {
                    try
                        {
                            Thread.sleep(pause);
                            SwingUtilities.invokeAndWait(closerRunner);
                        }
                    catch(Exception e)
                        {
                            e.printStackTrace();
                            // can catch InvocationTargetException
                            // can catch InterruptedException
                        }
                }
            };
        setVisible(true);
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        imagePanel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        imagePanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        imagePanel.add(imageLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(imagePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imageLabel;
    private javax.swing.JPanel imagePanel;
    // End of variables declaration//GEN-END:variables

}
