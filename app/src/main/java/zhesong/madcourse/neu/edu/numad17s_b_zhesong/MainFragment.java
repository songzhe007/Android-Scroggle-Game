/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package zhesong.madcourse.neu.edu.numad17s_b_zhesong;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;



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
      //View continueButton = rootView.findViewById(R.id.continue_button);
      View aboutButton = rootView.findViewById(R.id.about_button);
      newButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            getActivity().startActivity(intent);
         }
      });
//      rootView.findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View view) {
//            System.out.println("gameactivity exist?"+gameActivityExist());
//            Intent intent = new Intent(getActivity(), GameActivity.class);
//            //intent.putExtra(GameActivity.KEY_RESTORE, true);
//            getActivity().startActivity(intent);
//
//         }
//      });
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
      return rootView;

   }
   public boolean gameActivityExist(){
      Intent intent = new Intent();
      intent.setClassName("zhesong.madcourse.neu.edu.numad17s_zhesong", "GameActivity");
      return getActivity().getPackageManager().resolveActivity(intent, 0) == null;
   }
   @Override
   public void onPause() {
      super.onPause();

      // Get rid of the about dialog if it's still up
      if (mDialog != null)
         mDialog.dismiss();
   }
}
