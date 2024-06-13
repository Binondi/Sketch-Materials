package devs.org.sketch.material.dataModel

data class LocalLib(
    var key:String,
    var title:String,
    var downloadLink:String,
    var date:String,
    var description:String
){
    constructor():this("","","","","")
}
