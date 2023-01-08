# File Manager. ДЗ 2 по КПО

# Краткое описание

По сути, это программа для выявления зависимостей и вывода вложенных файлов.

В начале программа запрашивает путь к директории. Достаточно ввести что-то вроде "C:\Users\malce\OneDrive\Рабочий стол\kpotests". Программа на всякий случай показывает подсказку. В случае ввода несуществующей директории программа оповещает об этом пользователя.

Далее получаем выходной результат: выводится склеенное содержимое всех файлов в директории с учетом их сортировки по имени.

# ?Структура?

FilesAnalyzer.java - анализ файликов в директории и построение графа по ним

GraphOperations.java- собрание всех операций с графами

Ну и Main.java вместе с FileGraphNode.java - с помощью первого запускаем все, во втором прописаны всякие удобные штучки для работы с графами
