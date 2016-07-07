package com.g2inc.scap.editor.gui.windows.common;
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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author glenn.strickland
 */
public  class MouseClickListener implements MouseListener {

	protected boolean isRightClick = false;
    protected boolean isDoubleClick = false;

	public void doubleClicked(MouseEvent me) {
    }

	public void rightClicked(MouseEvent me) {
	}

    public void mouseClicked(MouseEvent me) {
		if (me.getClickCount() == 2) {
			doubleClicked(me);
        } else if (me.getButton() == MouseEvent.BUTTON2 || me.getButton() == MouseEvent.BUTTON3) {
            rightClicked(me);

        }
    }

	/**
	 * The init method with the booleans isRightClick and isDoubleClick is the OLD way this class
	 * was used. The user would override mouseClicked(), call init(), then call isRightClicked
	 * or isDoubleClicked to find out which was pressed.
	 *
	 * The NEW way is for the user to simply override the rightClicked() and/or doubleClicked() methods.
	 * @param me MouseEvent from MouseListener interface
	 */
    public void init(MouseEvent me) {
        if (me.getClickCount() == 2) {
            isDoubleClick = true;
        } else if (me.getButton() == MouseEvent.BUTTON2 || me.getButton() == MouseEvent.BUTTON3) {
            isRightClick = true;
        }
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

}
