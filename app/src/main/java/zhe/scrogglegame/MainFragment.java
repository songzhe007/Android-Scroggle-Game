
package zhe.scrogglegame;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




public class MainFragment extends Fragment {

   private AlertDialog mDialog;



   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View rootView =
            inflater.inflate(R.layout.fragment_main, container, false);

      rootView.findViewById(R.id.scoreboard_button).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(getActivity(), Scoreboard_Activity.class);
            getActivity().startActivity(intent);
         }
      });
      rootView.findViewById(R.id.leaderboard_button).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(getActivity(), Leaderboard_Activity.class);
            getActivity().startActivity(intent);
         }
      });
      // Handle buttons here...
      View newButton = rootView.findViewById(R.id.new_button);
      View aboutButton = rootView.findViewById(R.id.about_button);
      View multiPlayerButton = rootView.findViewById(R.id.multiPlayer_Button);

      newButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            getActivity().startActivity(intent);
         }
      });

      aboutButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.about_text);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.ok_label,
                  new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                     }
                  });
            mDialog = builder.show();
         }
      });
      multiPlayerButton.setOnClickListener(new View.OnClickListener(){
         @Override
         public void onClick(View view){
            Intent intent = new Intent(getActivity(),PickUserName.class);
            getActivity().startActivity(intent);
         }
      });
      return rootView;


   }


   @Override
   public void onPause() {
      super.onPause();
       if (mDialog != null)
         mDialog.dismiss();
   }
}
