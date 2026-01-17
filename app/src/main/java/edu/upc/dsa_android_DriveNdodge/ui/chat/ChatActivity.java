package edu.upc.dsa_android_DriveNdodge.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.ChatService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.ChatMessage;
import edu.upc.dsa_android_DriveNdodge.models.ChatRequest;
import edu.upc.dsa_android_DriveNdodge.models.MessageResponse;
import edu.upc.dsa_android_DriveNdodge.ui.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList;
    private EditText editTextInput;
    private Button buttonSend;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.reyclerview_message_list);
        editTextInput = findViewById(R.id.edittext_chatbox);
        buttonSend = findViewById(R.id.button_chatbox_send);
        progressBar = findViewById(R.id.progressBar);

        messageList = new ArrayList<>();

        messageList.add(new ChatMessage("Sistemas en línea. IA lista para asistencia.", false));

        adapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = editTextInput.getText().toString().trim();
        if (text.isEmpty()) return;

        messageList.add(new ChatMessage(text, true));
        updateList();
        editTextInput.setText("");

        progressBar.setVisibility(View.VISIBLE);
        buttonSend.setEnabled(false); // Evitar doble click

        ChatRequest request = new ChatRequest(text);

        ChatService service = RetrofitClient.getClient().create(ChatService.class);

        Call<MessageResponse> call = service.sendMessage(request);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                progressBar.setVisibility(View.GONE);
                buttonSend.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().getMessage();
                    messageList.add(new ChatMessage(reply, false));
                    updateList();
                } else {
                    ToastUtils.show(ChatActivity.this, "Error de comunicación: " + response.code(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                buttonSend.setEnabled(true);
                messageList.add(new ChatMessage("Error: Conexión perdida con el servidor central.", false));
                updateList();
            }
        });
    }
    private void updateList() {
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}