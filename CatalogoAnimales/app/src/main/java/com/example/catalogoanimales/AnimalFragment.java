package com.example.catalogoanimales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnimalFragment extends Fragment {

    private static final String ARG_ANIMAL = "selected_animal";
    private Animal animal;

    public static AnimalFragment newInstance(Animal animal) {
        AnimalFragment fragment = new AnimalFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ANIMAL, animal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            animal = (Animal) getArguments().getSerializable(ARG_ANIMAL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal, container, false);

        ImageView imageView = view.findViewById(R.id.imageViewAnimal);
        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        TextView textViewDesc = view.findViewById(R.id.textViewDescription);

        if (animal != null) {
            imageView.setImageResource(animal.getImageResId());
            textViewTitle.setText(animal.getName());
            textViewDesc.setText(animal.getDescription());
        }

        return view;
    }
}
