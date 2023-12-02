import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';
import 'app_data.dart';
import 'widget_selectable_list.dart';

class LayoutConnected extends StatefulWidget {
  const LayoutConnected({Key? key}) : super(key: key);

  @override
  State<LayoutConnected> createState() => _LayoutConnectedState();
}

class _LayoutConnectedState extends State<LayoutConnected> {
  //final ScrollController _scrollController = ScrollController();
  final _messageController = TextEditingController();
  final FocusNode _messageFocusNode = FocusNode();

  @override
  Widget build(BuildContext context) {
    AppData appData = Provider.of<AppData>(context);
    /*
    WidgetsBinding.instance.addPostFrameCallback((_) {
      //_scrollController.animateTo(
        //_scrollController.position.maxScrollExtent,
        duration: const Duration(milliseconds: 300),
        curve: Curves.easeOut,
      );
    });
    */

    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        middle: Text("WebSockets Client"),
      ),
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              "Esperando a más jugadores", // Reemplaza con el texto que deseas mostrar
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            CupertinoButton.filled(
              onPressed: () {
                // Lógica que quieras ejecutar al presionar el botón
                //appData.tuMetodo(); // Reemplaza con el método que deseas llamar
                appData.sendServer('{"type":"game","value":"start"}');
              },
              child: const Text(
                "Confirmar", // Reemplaza con el texto del botón
                style: TextStyle(fontSize: 14),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
