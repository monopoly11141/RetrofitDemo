package com.example.retrofitdemo

import Albums
import AlbumsItem
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var retrofitService: AlbumService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofitService =
            RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)

        //getRequestWithQueryParameters()
        //getRequestWithPathParameters()

        uploadAlbum()
    }

    private fun getRequestWithQueryParameters() {
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retrofitService.getSortedAlbums(3)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            albumsList?.let {
                while (albumsList.hasNext()) {
                    val albumsItem = albumsList.next()
                    val result = "${albumsItem.id} : ${albumsItem.title} : ${albumsItem.userId}\n"

                    val tvAlbums = findViewById<TextView>(R.id.tvAlbums)
                    tvAlbums.append(result)
                }
            }
        })
    }

    private fun getRequestWithPathParameters() {
        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.getAlbums(33)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show()

        })
    }

    private fun uploadAlbum() {
        val album = AlbumsItem(0, "My title", 256)

        val postResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.uploadAlbum(album)
            emit(response)
        }

        postResponse.observe(this, Observer {
            val receivedAlbumsItem = it.body()
            val result = "${receivedAlbumsItem?.id} : ${receivedAlbumsItem?.title} : ${receivedAlbumsItem?.userId}\n"
            val tvAlbums = findViewById<TextView>(R.id.tvAlbums)
            tvAlbums.text = result
        })
    }

}