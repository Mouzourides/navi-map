package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mouzourides.navi_map.DownloadImageTask;
import com.mouzourides.navi_map.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Nik Mouzourides on 18/03/2017.
 */
// PostcardFragment, allows user to send postcard to friends
public class PostcardFragment extends Fragment {
    private String locationName;
    private String locationImg;
    private EditText postcardNotes;
    private ImageView photo;

    public PostcardFragment() {
        // Required empty public constructor
    }

    // gets name and image via bundle from FavListFragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            locationName = bundle.getString("locationName", "");
            locationImg = bundle.getString("locationImg", "");
        }
    }
    //sets up view and components
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.postcard_layout, container, false);
        TextView textViewLocationName = (TextView)  view.findViewById(R.id.textViewLocationName3);
        textViewLocationName.setText(locationName);
        photo = (ImageView)  view.findViewById(R.id.image3);
        new DownloadImageTask(photo)
                .execute(locationImg);

         postcardNotes = (EditText) view.findViewById(R.id.editTextNotes);
        return view;
    }
    // button handler for share and upload button
    public void submitClickMethod(View v) {
        switch(v.getId()) {
            case R.id.buttonShare:
                photo.buildDrawingCache();
                Bitmap bm= photo.getDrawingCache();
                Uri imageUri =  getImageUri(getContext(), bm);
                String strNotes = locationName + "\n" + postcardNotes.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, strNotes);
                sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                sendIntent.setType("image/jpeg");
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));

            break;
            case R.id.buttonUpload:
                Toast.makeText(getContext(), "Upload", Toast.LENGTH_LONG).show();
                break;
        }
    }
    // bitmap to uri converter
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}

