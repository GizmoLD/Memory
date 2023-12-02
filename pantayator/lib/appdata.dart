import 'dart:convert';
import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:web_socket_channel/io.dart';
import 'package:pantayator/pages/playingroom.dart';

class AppData with ChangeNotifier {
  Random r = new Random();
  String ip = "";
  String text = "";
  String port = "";
  bool connected = false;
  String userName = "";
  bool playing = false;
  List<Fruit> fruits = [];

  //WebSocket
  IOWebSocketChannel? _server;

  Future<void> connectServer(BuildContext context) async {
    if (!connected) {
      _server = IOWebSocketChannel.connect("ws://$ip:$port");
      print("Se hizo la conexion");
      connected = true;
      notifyListeners();
      //Quan rep un missatge
      _server!.stream.listen(
        (message) {
          final data = jsonDecode(message);

          switch (data['type']) {
            case 'deck':
              print(data);
              // Procesar la información del 'deck'
              if (data['value'] != null) {
                // Limpiar la lista existente de frutas
                fruits.clear();
                // Obtener la lista de frutas del mensaje
                List<dynamic> deckData = data['value'];
                // Almacenar las frutas en la lista
                for (var fruitData in deckData) {
                  Fruit fruit = Fruit(
                    fruit: fruitData['fruit'],
                    id: fruitData['id'],
                  );
                  fruits.add(fruit);
                }
              }
              print(fruits.isEmpty);
              print(fruits);
              // Navegar a PlayingRoom si no se está jugando ya
              if (!playing) {
                Navigator.push(
                  context,
                  CupertinoPageRoute(builder: (context) => PlayingRoom()),
                );
                playing = true;
              }
              break;
          }

          notifyListeners();
        },
        onError: (error) {
          //Si dona un error es reinicia tot
          connected = false;
          notifyListeners();
        },
        onDone: () {
          //Quan el server es desconecta
          connected = false;
          notifyListeners();
        },
      );
    }
  }

  //Si es vol desconectar del server
  disconnectedFromServer() async {
    connected = false;
    notifyListeners();
    _server!.sink.close();
  }

  //Mandar missatge per a que ho print
  void showTextMessage() {
    //final msn = {'type': 'name', 'value': text, 'user': userName};
    final msn = {'type': 'name', 'value': userName};
    _server?.sink.add(jsonEncode(msn));
  }

  startGame() {
    final msn = {'type': 'game', 'value': 'start'};
    _server?.sink.add(jsonEncode(msn));
  }

  sendCard(int i) {
    final msn = {'type': 'game', 'value': 'start'};
    _server?.sink.add(jsonEncode(msn));
  }
}

//Clase para representar una fruta
class Fruit {
  final String fruit;
  final int id;

  Fruit({required this.fruit, required this.id});
}
