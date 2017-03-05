package mobi.tarantino.stub.auto.feature.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.tarantino.stub.auto.R;

/**

 */

public class AddDocumentChooser extends DialogFragment {

    @BindView(R.id.add_car_button)
    Button addCarButton;

    @BindView(R.id.add_driver_button)
    Button addDriverButton;

    private ShowAddDocumentListener addDocumentListener;

    public static AddDocumentChooser getInstance() {
        return new AddDocumentChooser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDocumentListener = (ShowAddDocumentListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_add_documents, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.add_car_button)
    public void onAddCarButtonClick() {
        addDocumentListener.showAddingVehicleRegistrationScreen();
        dismiss();
    }

    @OnClick(R.id.add_driver_button)
    public void onAddDriverButtonClick() {
        addDocumentListener.showAddingDriverLicenseScreen();
        dismiss();
    }
}
