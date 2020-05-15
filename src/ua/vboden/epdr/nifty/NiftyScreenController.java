package ua.vboden.epdr.nifty;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import ua.vboden.epdr.AppContext;

public class NiftyScreenController implements ScreenController {

	private Nifty nifty;
	private AppContext context;

	public NiftyScreenController(AppContext context) {
		this.context = context;
	}

	@Override
	public void bind(Nifty nifty, Screen arg1) {
		this.nifty = nifty;
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

	public void close() {
		nifty.closePopup(context.getNiftyManager().getPopupId());
	}
}
