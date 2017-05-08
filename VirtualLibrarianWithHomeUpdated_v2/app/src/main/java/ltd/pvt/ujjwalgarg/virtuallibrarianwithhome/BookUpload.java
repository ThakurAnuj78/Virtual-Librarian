package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Naman on 22-Nov-16.
 */

public class BookUpload extends Fragment {
    EditText isbn,category,subcategory,title,publisher,author;
    Button choose,upload;
    Uri path;
    Bitmap image;
    ImageView im;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.book_upload,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isbn= (EditText) getActivity().findViewById(R.id.isbn);
        category= (EditText) getActivity().findViewById(R.id.category);
        subcategory= (EditText) getActivity().findViewById(R.id.sub_category);
        title= (EditText) getActivity().findViewById(R.id.book_title);
        publisher= (EditText) getActivity().findViewById(R.id.publication);
        author= (EditText) getActivity().findViewById(R.id.author);
        choose= (Button) getActivity().findViewById(R.id.choose_image);
        upload= (Button) getActivity().findViewById(R.id.upload);
        im= (ImageView) getActivity().findViewById(R.id.bookview);
    }

    @Override
    public void onStart() {

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"select picture"),1);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String isb,cat,sub,img,aut,pub,bname;
                isb=isbn.getText().toString();
                cat=category.getText().toString();
                sub=subcategory.getText().toString();
                aut=author.getText().toString();
                pub=publisher.getText().toString();
                bname=title.getText().toString();
                img=getStringImage(image);
                StringRequest request = new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/upload.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success"))
                                {
                                    Toast.makeText(getActivity(), "Image successfully uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map= new HashMap<String, String>();
                        map.put("isbn",isb);
                        map.put("image",img);
                        map.put("cat",cat);
                        map.put("sub",sub);
                        map.put("author",aut);
                        map.put("pub",pub);
                        map.put("name",bname);


                        return map;
                    }
                };
                RequestQueue r = Volley.newRequestQueue(getActivity());
                r.add(request);

            }
        });
        super.onStart();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            path=data.getData();
            try{
                image= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                im.setImageBitmap(image);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
