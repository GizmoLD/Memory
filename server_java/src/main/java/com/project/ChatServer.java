package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatServer extends WebSocketServer {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String jsonString = "{ \"type\": \"deck\", \"value\": [ {\"type\": \"card\", \"fruit\": \"naranja\", \"image\": \"naranja.jpg\", \"id\": 1}, {\"type\": \"card\", \"fruit\": \"naranja\", \"image\": \"naranja.jpg\", \"id\": 2}, {\"type\": \"card\", \"fruit\": \"uva\", \"image\": \"uva.jpg\", \"id\": 3}, {\"type\": \"card\", \"fruit\": \"uva\", \"image\": \"uva.jpg\", \"id\": 4}, {\"type\": \"card\", \"fruit\": \"manzana\", \"image\": \"manzana.jpg\", \"id\": 5}, {\"type\": \"card\", \"fruit\": \"manzana\", \"image\": \"manzana.jpg\", \"id\": 6}, {\"type\": \"card\", \"fruit\": \"pera\",\"image\": \"pera.jpg\", \"id\": 7}, {\"type\": \"card\", \"fruit\": \"pera\",\"image\": \"pera.jpg\", \"id\": 8}, {\"type\": \"card\", \"fruit\": \"sandia\",\"image\": \"sandia.jpg\", \"id\": 9}, {\"type\": \"card\", \"fruit\": \"sandia\",\"image\": \"sandia.jpg\", \"id\": 10}, {\"type\": \"card\", \"fruit\": \"platano\", \"image\": \"platano.jpg\", \"id\": 11}, {\"type\": \"card\", \"fruit\": \"platano\", \"image\": \"platano.jpg\", \"id\": 12}, {\"type\": \"card\", \"fruit\": \"fresa\",\"image\": \"fresa.jpg\", \"id\": 13}, {\"type\": \"card\", \"fruit\": \"fresa\",\"image\": \"fresa.jpg\", \"id\": 14}, {\"type\": \"card\", \"fruit\": \"kiwi\",\"image\": \"kiwi.jpg\", \"id\": 15}, {\"type\": \"card\", \"fruit\": \"kiwi\",\"image\": \"kiwi.jpg\", \"id\": 16} ] }";
    //String jsonString = "{ \"type\": [ {\"type\": \"card\", \"fruit\": \"naranja\", \"image\": \"naranja.jpg\", \"id\": \"card00\"}, {\"type\": \"card\", \"fruit\": \"naranja\", \"image\": \"naranja.jpg\", \"id\": \"card01\"}, {\"type\": \"card\", \"fruit\": \"uva\", \"image\": \"uva.jpg\", \"id\": \"card02\"}, {\"type\": \"card\", \"fruit\": \"uva\", \"image\": \"uva.jpg\", \"id\": 4}, {\"type\": \"card\", \"fruit\": \"manzana\", \"image\": \"manzana.jpg\", \"id\": 5}, {\"type\": \"card\", \"fruit\": \"manzana\", \"image\": \"manzana.jpg\", \"id\": 6}, {\"type\": \"card\", \"fruit\": \"pera\",\"image\": \"pera.jpg\", \"id\": 7}, {\"type\": \"card\", \"fruit\": \"pera\",\"image\": \"pera.jpg\", \"id\": 8}, {\"type\": \"card\", \"fruit\": \"sandia\",\"image\": \"sandia.jpg\", \"id\": 9}, {\"type\": \"card\", \"fruit\": \"sandia\",\"image\": \"sandia.jpg\", \"id\": 10}, {\"type\": \"card\", \"fruit\": \"platano\", \"image\": \"platano.jpg\", \"id\": 11}, {\"type\": \"card\", \"fruit\": \"platano\", \"image\": \"platano.jpg\", \"id\": 12}, {\"type\": \"card\", \"fruit\": \"fresa\",\"image\": \"fresa.jpg\", \"id\": 13}, {\"type\": \"card\", \"fruit\": \"fresa\",\"image\": \"fresa.jpg\", \"id\": 14}, {\"type\": \"card\", \"fruit\": \"kiwi\",\"image\": \"kiwi.jpg\", \"id\": 15}, {\"type\": \"card\", \"fruit\": \"kiwi\",\"image\": \"kiwi.jpg\", \"id\": 16} ] }";
    ArrayList<ArrayList<String>> users = new ArrayList(); // conn, point, name ,inicio
    ArrayList<ArrayList<String>> selectedCards = new ArrayList<>();
    ArrayList<Boolean> confirmacionDeInicio = new ArrayList(); // se envia un inicio
    boolean partidaIniciada = false;
    int totalPoints = 8;

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
        // Quan el servidor s'inicia
        String host = getAddress().getAddress().getHostAddress();
        int port = getAddress().getPort();
        System.out.println("WebSockets server running at: ws://" + host + ":" + port);
        System.out.println("Type 'exit' to stop and exit server.");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // Quan un client es connecta
        String clientId = getConnectionId(conn);

        // contabiliza la cantidad de usurios conectados
        ArrayList user = new ArrayList<>();
        user.add(clientId);
        user.add("0");
        user.add("false");
        users.add(user);

        // Saludem personalment al nou client
        JSONObject objWlc = new JSONObject("{}");
        objWlc.put("type", "private");
        objWlc.put("from", "server");
        objWlc.put("value", "Welcome to the chat server");
        conn.send(objWlc.toString()); 

        // Li enviem el seu identificador
        JSONObject objId = new JSONObject("{}");
        objId.put("type", "id");
        objId.put("from", "server");
        objId.put("value", clientId);
        conn.send(objId.toString()); 

        // Enviem al client la llista amb tots els clients connectats
        sendList(conn);

        // Enviem la direcció URI del nou client a tothom 
        JSONObject objCln = new JSONObject("{}");
        objCln.put("type", "connected");
        objCln.put("from", "server");
        objCln.put("id", clientId);
        broadcast(objCln.toString());
        

        // Mostrem per pantalla (servidor) la nova connexió
        String host = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        System.out.println("New client (" + clientId + "): " + host);
    }

    public void iniciarPartida(String conn) {
        for (ArrayList<String> u : users) {
            if (u.get(0).equalsIgnoreCase(conn)){
                u.set(2, "start");
            }
            if (u.get(2).equals("false")) {
                return;
            }
            partidaIniciada = true;
        }
        if (partidaIniciada) {
            System.out.println("Se comenzooo");
            Collections.shuffle(users);
            randomDeck();
            //sendDeck();
            sendTurn();
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // Quan un client es desconnecta
        String clientId = getConnectionId(conn);
        ArrayList<ArrayList<String>> usuarios = new ArrayList<>();

        // Informem a tothom que el client s'ha desconnectat
        JSONObject objCln = new JSONObject("{}");
        objCln.put("type", "disconnected");
        objCln.put("from", "server");
        objCln.put("id", clientId);
        /*
         * enviar mensaje diciendo que el otro jugador a ganado
         */
        broadcast(objCln.toString());

        // Mostrem per pantalla (servidor) la desconnexió
        System.out.println("Client disconnected '" + clientId + "'");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        String clientId = getConnectionId(conn);
        try {
            System.out.println(message);
            JSONObject objRequest = new JSONObject(message);
            String type = objRequest.getString("type");

            // REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR
            if (type.equalsIgnoreCase("name")) {
                /* 
                if (users.size() == 1) {
                    String name = objRequest.getString("value");
                    users.get(0).add(name);
                } else if (users.size() == 2) {
                    String name = objRequest.getString("value");
                    users.get(1).add(name);
                }
                */
                /* 
                for (int i = 0; i < users.size(); i++){
                    String name = objRequest.getString("value");
                    users.get(i).add(name);
                }
                */
                String name = objRequest.getString("value");
                users.get(users.size()-1).add(name);
            }
            // REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR REVISAR

            if (type.equalsIgnoreCase("game")) {
                System.out.println("Llego un inicio");
                String value = objRequest.getString("value");
                // inicio de partida
                // {"type":"game","value":"start"}
                if (value.equalsIgnoreCase("start")) {
                    System.out.println("Confirmo");
                    iniciarPartida(clientId);
                    System.out.println("comenzo");
                }
            }

            if (type.equalsIgnoreCase("card")) {
                String fruit = objRequest.getString("fruit");
                String id = objRequest.getString("id");
                // turno
                if (selectedCards.size() == 0) {
                    ArrayList<String> card = new ArrayList<>();
                    card.add(fruit);
                    card.add(id);
                    selectedCards.add(card);
                } else if (selectedCards.size() == 1) {
                    ArrayList<String> card = new ArrayList<>();
                    card.add(fruit);
                    card.add(id);
                    selectedCards.add(card);

                    // comprueba que que las cartas son iguales y que tienen un diferente id (punto)
                    if (selectedCards.get(0).get(0).equals(selectedCards.get(1).get(0)) &&
                            selectedCards.get(0).get(1) != selectedCards.get(1).get(1)) {
                        addPoint();
                        sendUsersData();
                        // selecciono 2 veces la misma carta
                    } else if (selectedCards.get(0).get(0).equals(selectedCards.get(1).get(0)) &&
                            selectedCards.get(0).get(1) == selectedCards.get(1).get(1)) {
                        sendUsersData();
                        // no fue punto
                    } else {
                        sendUsersData();
                    }
                    // cambio de turno y limpieza de cartas seleccionadas
                    if (totalPoints == 0) {
                        endGame();
                    } else {
                        nextPlayer();
                        sendTurn();
                        selectedCards.clear();
                    }
                }
            } else if (type.equalsIgnoreCase("private")) {
                // El client envia un missatge privat a un altre client
                System.out.println("Client '" + clientId + "'' sends a private message");

                JSONObject objResponse = new JSONObject("{}");
                objResponse.put("type", "private");
                objResponse.put("from", clientId);
                objResponse.put("value", objRequest.getString("value"));

                String destination = objRequest.getString("destination");
                WebSocket desti = getClientById(destination);

                if (desti != null) {
                    desti.send(objResponse.toString());
                }

            } else if (type.equalsIgnoreCase("broadcast")) {
                // El client envia un missatge a tots els clients
                System.out.println("Client '" + clientId + "'' sends a broadcast message to everyone");

                JSONObject objResponse = new JSONObject("{}");
                objResponse.put("type", "broadcast");
                objResponse.put("from", clientId);
                objResponse.put("value", objRequest.getString("value"));
                broadcast(objResponse.toString());
            } else if (type.equalsIgnoreCase("user")) {
                if (users.size() == 1) {
                    users.get(0).add(objRequest.getString("value"));
                } else if (users.size() == 2) {
                    users.get(1).add(objRequest.getString("value"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endGame() {
        partidaIniciada = false;
        for (ArrayList<String> user : users) {
            user.set(4, "false");
        }
        orderUsersByScoreDesc();
        JSONObject objResponse = new JSONObject("{}");
        objResponse.put("type", "game");
        objResponse.put("value", "gameOver");
        JSONArray usersArray = new JSONArray();
        for (ArrayList<String> user : users) {
            JSONObject userObj = new JSONObject();
            userObj.put("id", user.get(0));
            userObj.put("score", user.get(1));
            userObj.put("name", user.get(2));
            usersArray.put(userObj);
        }
        objResponse.put("users", usersArray);
        broadcast(objResponse.toString());
    }

    private void orderUsersByScoreDesc() {
        Collections.sort(users, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> user1, ArrayList<String> user2) {
                int score1 = Integer.parseInt(user1.get(1));
                int score2 = Integer.parseInt(user2.get(1));
                return Integer.compare(score2, score1); // Orden descendente
            }
        });
    }

    public void sendUsersData() {
        JSONObject objResponse = new JSONObject("{}");
        objResponse.put("type", "game");
        JSONArray usersArray = new JSONArray();
        for (ArrayList<String> user : users) {
            JSONObject userObj = new JSONObject();
            userObj.put("id", user.get(0));
            userObj.put("score", user.get(1));
            userObj.put("name", user.get(2));
            usersArray.put(userObj);
        }
        objResponse.put("users", usersArray);
        broadcast(objResponse.toString());
    }

    public void addPoint() {
        String pointAsString = users.get(0).get(1);
        int score = Integer.parseInt(pointAsString);
        score += 1;
        users.get(0).set(1, String.valueOf(score));
        totalPoints -= 1;
    }

    public void nextPlayer() {
        if (users.size() > 0) {
            ArrayList<String> firstList = users.get(0);
            ArrayList<ArrayList<String>> listaUsuarios = new ArrayList<>();
            for (ArrayList<String> user : users) {
                listaUsuarios.add(user);
            }
            listaUsuarios.add(firstList);
            users = listaUsuarios;
        } else {
            System.out.println("No hay jugadores");
        }
    }

    public void turnCards() {
        // `{"type":"play","value":"turnAll","id":[4,5]}
        JSONObject objResponse = new JSONObject("{}");
        objResponse.put("type", "play");
        objResponse.put("value", "turnAll");

        JSONArray idArray = new JSONArray();
        for (ArrayList<String> card : selectedCards) {
            if (!card.isEmpty() && card.size() > 0) {
                idArray.put(Integer.parseInt(card.get(0)));
            }
        }
        objResponse.put("id", idArray);
        broadcast(objResponse.toString());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        // Quan hi ha un error
        ex.printStackTrace();
    }

    public void runServerBucle() {
        boolean running = true;
        try {
            System.out.println("Starting server");
            start();
            while (running) {
                String line;
                line = in.readLine();
                if (line.equals("exit")) {
                    running = false;
                }
            }
            System.out.println("Stopping server");
            stop(1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendList(WebSocket conn) {
        JSONObject objResponse = new JSONObject("{}");
        objResponse.put("type", "list");
        objResponse.put("from", "server");
        objResponse.put("list", getClients());
        conn.send(objResponse.toString());
    }

    public String getConnectionId(WebSocket connection) {
        String name = connection.toString();
        return name.replaceAll("org.java_websocket.WebSocketImpl@", "").substring(0, 3);
    }

    public String[] getClients() {
        int length = getConnections().size();
        String[] clients = new String[length];
        int cnt = 0;

        for (WebSocket ws : getConnections()) {
            clients[cnt] = getConnectionId(ws);
            cnt++;
        }
        return clients;
    }

    public WebSocket getClientById(String clientId) {
        for (WebSocket ws : getConnections()) {
            String wsId = getConnectionId(ws);
            if (clientId.compareTo(wsId) == 0) {
                return ws;
            }
        }
        return null;
    }

    public void randomDeck() {
        try {
            // Parse JSON string
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("value");
    
            // Convert JSONArray to List
            List<Object> cardList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                cardList.add(jsonArray.get(i));
            }
    
            // Shuffle the list
            Collections.shuffle(cardList);
    
            // Create a new JSONArray and add the shuffled elements
            JSONArray shuffledJsonArray = new JSONArray(cardList);
    
            // Update the original JSONObject with the shuffled JSONArray
            jsonObject.put("type", "deck");
            jsonObject.put("value", shuffledJsonArray);
    
            // Convert back to JSON string
            String randomizedJsonString = jsonObject.toString();
    
            // Print the randomized JSON string
            System.out.println(randomizedJsonString);
    
            // Enviar el mazo a los clientes
            sendDeck(randomizedJsonString);
    
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendTurn() {
        JSONObject objResponse = new JSONObject("{}");
        objResponse = new JSONObject("{}");
        objResponse.put("type", "game");
        objResponse.put("turn", users.get(0).get(0));
        broadcast(objResponse.toString()); 
    }

    public void sendDeck(String shuffledDeck) {
        broadcast(shuffledDeck);
    }
}