import java.io.File

class FolderReader(var path: String){
    fun read() : ArrayList<String>{
        var buffer : ArrayList<String> = arrayListOf()

        File(path).walk().forEach {
            if(!it.toString().contains('[') || !it.toString().contains(']')){
                buffer.add(it.toString())
            }
        }

        return buffer
    }
}