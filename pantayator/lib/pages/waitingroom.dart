import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pantayator/appdata.dart';
import 'package:provider/provider.dart';

class WaitingRoom extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        middle: Text('Sala de Espera'),
      ),
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Esperando a más jugadores',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 16),
            CupertinoButton(
              onPressed: () {
                // Llamar a la función startGame cuando se presiona el botón
                Provider.of<AppData>(context, listen: false).startGame();
              },
              child: Text('Iniciar partida'),
            ),
          ],
        ),
      ),
    );
  }
}
/*
                  var appData = Provider.of<AppData>(context, listen: false);
                  appData.ip = ip;
                  appData.port = port;
                  appData.userName = name;

                  await appData.connectServer(context);

                  if (appData.connected) {
                    // Navegar a WaitingRoom solo si la conexión fue exitosa
                    appData.showTextMessage();
                    Navigator.push(
                      context,
                      CupertinoPageRoute(builder: (context) => WaitingRoom()),
                    );
                  } else {
                    print('No se pudo conectar al servidor.');
                  }
*/