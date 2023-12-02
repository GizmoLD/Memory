import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';
import 'package:pantayator/appdata.dart';
import 'waitingroom.dart';

class Lobby extends StatelessWidget {
  final TextEditingController ipController = TextEditingController();
  final TextEditingController portController = TextEditingController();
  final TextEditingController nameController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        middle: Text('Lobby'),
      ),
      child: Center(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              _createTextField('IP', 'Ingrese la dirección IP', ipController),
              _createTextField('Puerto', 'Ingrese el puerto', portController),
              _createTextField('Nombre', 'Ingrese su nombre', nameController),
              SizedBox(height: 16),
              CupertinoButton(
                onPressed: () async {
                  String ip = ipController.text;
                  String port = portController.text;
                  String name = nameController.text;

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
                },
                child: Text('Conectar al servidor'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Column _createTextField(
      String title, String defaultText, TextEditingController controller) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Text(
          title,
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 28),
        ),
        SizedBox(
          height: 16,
        ),
        Container(
          width: 225,
          child: CupertinoTextField(
            decoration: BoxDecoration(
              border: Border.all(color: Color.fromARGB(255, 105, 105, 105)),
              borderRadius: BorderRadius.all(Radius.circular(8)),
            ),
            padding: EdgeInsets.all(10),
            textAlign: TextAlign.left,
            placeholder: defaultText,
            controller: controller,
            style: TextStyle(fontSize: 16.0),
          ),
        ),
      ],
    );
  }
}
