package br.senai.sp.informatica.listadejogos.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.Optional;

/**
 * Created by WEB on 31/10/2017.
 */

public class JogoDao {
    public static JogoDao manager = new JogoDao();
    private List<Jogo> lista;
    private long id = 0;

    private JogoDao(){
       lista = new ArrayList<>();
//        lista.add(new Jogo(id++,"Dota", "Moba"));
//        lista.add(new Jogo(id++,"Battlefield 1", "FPS"));
//        lista.add(new Jogo(id++,"Age of Mythology", "RTS"));

        lista.add(new Jogo(id++,"Dota", "Moba"));
        lista.add(new Jogo(id++,"Battlefield 1", "FPS"));
        lista.add(new Jogo(id++,"Age of Mythology", "RTS"));

//        lista.add(new Jogo(id++,"World of Warcraft", "MMORPG"));
//        lista.add(new Jogo(id++,"HeartStone", "Card Game"));
//        lista.add(new Jogo(id++,"The Evil Within", "Suvivor Horror"));
//        lista.add(new Jogo(id++,"South Park The Fractured But Whole", "RPG"));

    }

    public List<Jogo> getLista(){

        Collections.sort(lista);
        return Collections.synchronizedList(lista);
    }

    public Jogo getJogo(Long id){
        Jogo game =null;
        for (Jogo jogo: lista) {
            if (jogo.getId() == id){
                game = jogo;
                break;
            }
        }

        Jogo jogolocalizado = lista.get(lista.indexOf(new Jogo(id)));

       // Jogo outroJogo = lista.stream().filter(jogo -> jogo.getId() == id).findAny().orElse(null);

        return game;
    }
    public void salvar(Jogo jogo){
        if(jogo.getId() == null){
            jogo.setId(id++);
            lista.add(jogo);
        }else{
           int posicao = lista.indexOf(new Jogo(jogo.getId()));
            lista.set(posicao, jogo);
        }
    }

    public void apagar(Long id){
        lista.remove(new Jogo(id));
    }


}
