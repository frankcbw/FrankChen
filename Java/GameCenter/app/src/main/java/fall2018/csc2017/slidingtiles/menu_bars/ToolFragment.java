package fall2018.csc2017.slidingtiles.menu_bars;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fall2018.csc2017.slidingtiles.LoginActivity;
import fall2018.csc2017.slidingtiles.R;

/**
 * The Fragment of the Tool page.
 */
public class ToolFragment extends Fragment {
    Button logOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tool_fragment, container, false);
        logOut = rootView.findViewById(R.id.logOut1);
        addLogOut();
        return rootView;
    }

    /**
     * Sign the current user out and turn to the login page.
     */
    private void addLogOut() {
        logOut.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(getActivity(), LoginActivity.class);
                                          startActivity(intent);
                                      }
                                  }
        );
    }
}
