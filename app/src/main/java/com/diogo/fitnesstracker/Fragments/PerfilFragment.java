package com.diogo.fitnesstracker.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.diogo.fitnesstracker.Manifest;
import com.diogo.fitnesstracker.R;
import com.diogo.fitnesstracker.adapter.adapterListaPerfil;
import com.diogo.fitnesstracker.config.ConfiguracaoFirebase;
import com.diogo.fitnesstracker.helper.CodificadorBase64;
import com.diogo.fitnesstracker.model.ItemsRecycler;
import com.diogo.fitnesstracker.model.Perfil;
import com.diogo.fitnesstracker.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private adapterListaPerfil adapterListaPerfil;
    private List<ItemsRecycler> listaPerfil = new ArrayList<>();

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaFirebase();
    private DatabaseReference perfilRef;
    private ValueEventListener valueEventListenerPerfil;
    private StorageReference storage = ConfiguracaoFirebase.getFirebaseStorage();

    private TextView dadosAltura,dadosPeso,dadosObjetivo,dadosNome,textViewAltura,textViewPeso,textViewObjetivo;
    private ImageView imageViewPerfil;
    private ProgressBar progressBar;

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_perfil, container, false);
        dadosAltura = v.findViewById(R.id.dadosAltura);
        dadosPeso = v.findViewById(R.id.dadosPeso);
        dadosObjetivo = v.findViewById(R.id.dadosObjetivo);
        dadosNome = v.findViewById(R.id.dadosNome);
        progressBar = v.findViewById(R.id.progressBar);
        textViewAltura = v.findViewById(R.id.textViewAltura);
        textViewObjetivo = v.findViewById(R.id.textViewObjetivo);
        textViewPeso = v.findViewById(R.id.textViewPeso);
        imageViewPerfil = v.findViewById(R.id.imageView);

        FirebaseUser utilizador = ConfiguracaoFirebase.getAutenticacao().getCurrentUser();
        Uri url = utilizador.getPhotoUrl();

        if(url != null)
        {
            Glide.with(getActivity()).load(url).into(imageViewPerfil);
        }



        imageViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Escolha uma opção").setItems(R.array.items_fotografia, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //verificar qual das opcoes foi escolhida
                        switch (which)
                        {
                            case 0:
                                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if(i.resolveActivity(getActivity().getPackageManager()) != null)
                                {
                                startActivityForResult(i, SELECAO_CAMERA); //captura resultado de retorno
                                }
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                if(intent.resolveActivity(getActivity().getPackageManager()) != null)
                                {
                                    startActivityForResult(intent, SELECAO_GALERIA); //captura resultado de retorno
                                }
                                break;
                        }
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recyclerView = v.findViewById(R.id.recyclerViewPerfil);
        adapterListaPerfil = new adapterListaPerfil(getContext(),listaPerfil);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterListaPerfil);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtemDados();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK)
        {
            Bitmap imagem = null;

            try {

                switch (requestCode)
                {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                        break;
                }
                if(imagem != null)
                {
                    imageViewPerfil.setImageBitmap(imagem);

                    //Obter dados da imagem para a firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                    byte[] dadosImagem = baos.toByteArray();

                    // gravar imagem na firebase
                    String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
                    StorageReference imagemRef = storage.child("imagens").child("perfil").child(IDUtilizador).child("perfil.jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Occoreu um erro ao fazer upload da imagem",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(),"Sucesso ao fazer upload da imagem",Toast.LENGTH_SHORT).show();
                            Uri url = taskSnapshot.getDownloadUrl();
                            atualizaFoto(url);
                        }
                    });
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void atualizaFoto(Uri url)
    {
        FirebaseUser user = ConfiguracaoFirebase.getAutenticacao().getCurrentUser();
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();

        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful())
                {
                    Log.d("Perfil","Erro ao atualizar a foto de perfil.");
                }
            }
        });
    }

    public void obtemDados()
    {

        String IDUtilizador = CodificadorBase64.codificaBase64(autenticacao.getCurrentUser().getEmail());
        perfilRef = firebaseRef.child("Utilizadores").child(IDUtilizador);
        valueEventListenerPerfil = perfilRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaPerfil.clear();
                Utilizador utilizador = dataSnapshot.getValue(Utilizador.class);
                String campoAltura = Double.toString((int) utilizador.getAltura());
                String campoPeso = Double.toString(utilizador.getPeso());
                listaPerfil.add(new ItemsRecycler("Nome",utilizador.getNome()));
                listaPerfil.add(new ItemsRecycler("Altura",Double.toString(utilizador.getAltura()) + " cm"));
                listaPerfil.add(new ItemsRecycler("Peso",Double.toString(utilizador.getPeso()) + " kg"));
                listaPerfil.add(new ItemsRecycler("Genero",utilizador.getSexo()));
                listaPerfil.add(new ItemsRecycler("Data de nascimento",utilizador.getData_nascimento()));
                listaPerfil.add(new ItemsRecycler("E-mail",utilizador.getEmail()));
                dadosAltura.setText(campoAltura);
                dadosPeso.setText(campoPeso);
                dadosNome.setText(utilizador.getNome());
                adapterListaPerfil.notifyDataSetChanged();

                mostraLayout();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void mostraLayout()
    {
        progressBar.setVisibility(View.GONE);
        dadosAltura.setVisibility(View.VISIBLE);
        dadosPeso.setVisibility(View.VISIBLE);
        dadosObjetivo.setVisibility(View.VISIBLE);
        dadosNome.setVisibility(View.VISIBLE);
        textViewAltura.setVisibility(View.VISIBLE);
        textViewObjetivo.setVisibility(View.VISIBLE);
        textViewPeso.setVisibility(View.VISIBLE);
        imageViewPerfil.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        perfilRef.removeEventListener(valueEventListenerPerfil);
    }
}
