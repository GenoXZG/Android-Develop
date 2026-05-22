package com.example.catalogoanimales;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewAnimals);
        loadAnimals();

        List<String> names = new ArrayList<>();
        for (Animal animal : animalList) {
            names.add(animal.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                names
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Animal selectedAnimal = animalList.get(position);
            replaceFragment(selectedAnimal);
        });

        if (savedInstanceState == null && !animalList.isEmpty()) {
            replaceFragment(animalList.get(0));
        }
    }

    private void loadAnimals() {
        animalList = new ArrayList<>();

        animalList.add(new Animal(
                getString(R.string.titulo_perro),
                R.drawable.perro,
                getString(R.string.descripcion_perro)
        ));

        animalList.add(new Animal(
                getString(R.string.titulo_gato),
                R.drawable.gato,
                getString(R.string.descripcion_gato)
        ));

        animalList.add(new Animal(
                getString(R.string.titulo_vaca),
                R.drawable.vaca,
                getString(R.string.descripcion_vaca)
        ));
    }
    private void replaceFragment(Animal animal) {
        AnimalFragment fragment = AnimalFragment.newInstance(animal);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
