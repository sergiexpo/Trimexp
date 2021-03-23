package com.neoland.trimexp.entities

class Experience (var title: String,
                  var mainPhoto: Int,
                  var description: String,
                  var location: String,
                  var adress: String = " ",
                  var duration: String,
                  var opinions: MutableList<String> = mutableListOf<String>(),
                  var isNovelty: Boolean,
                  var typeExperience: String, // Host o Guest
                  var languages: MutableList<String> = mutableListOf<String>(),
                  var price: String,
                  var paymentMethods: MutableList<String> = mutableListOf<String>(),
                  var divisa: String = " ",
                  var owner: String,            // Aqui habrá que poner que sea de la clase USER
                  var requester: String?      // Aqui habrá que poner que sea de la clase USER
                  ) {
}