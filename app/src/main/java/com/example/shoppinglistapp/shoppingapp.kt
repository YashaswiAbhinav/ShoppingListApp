package com.example.shoppinglistapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly

data class ShoppingItems(
    val id:Int,
    var name:String,
    var quantity: Int,


)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Index(){
    var items by remember { mutableStateOf(listOf<ShoppingItems>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemNameInput by remember { mutableStateOf("") }
    var itemQuantityInput by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),


        ){
        Button(
            onClick = {showDialog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add items")
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
        ){
            items(items) {
                ItemCard(
                    item = it,
                    onDelete = { itemToDelete ->
                        items = items.filter { it.id != itemToDelete.id }
                    }
                )
            }
        }
    }

    if(showDialog){
        BasicAlertDialog(onDismissRequest = {showDialog = !showDialog}) {
            Surface(
                modifier = Modifier
                    .height(400.dp)
                    .width(300.dp),
                contentColor = Color.Black,
                shape = RoundedCornerShape(corner = CornerSize(32.dp))

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.Center,
                    modifier = Modifier.background(Color(160,200,225))
                ) {
                    Text(modifier = Modifier.padding(top = 16.dp),text = "Add Item", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(64.dp))
                    OutlinedTextField(
                        shape = RoundedCornerShape(corner = CornerSize(64.dp)),
                        value = itemNameInput,
                        onValueChange = { itemNameInput = it },
                        placeholder = { Text("Item Name", color = Color.Black) }

                    )
                    Spacer(Modifier.height(24.dp))
                    OutlinedTextField(
                        shape = RoundedCornerShape(corner = CornerSize(64.dp)),
                        value = itemQuantityInput,
                        onValueChange = { itemQuantityInput = it },
                        placeholder = { Text("Item Qantity", color = Color.Black) }

                    )
                    Spacer(Modifier.height(64.dp))
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {

                        OutlinedButton(
                            onClick = {showDialog = false},
                            modifier = Modifier
                                .weight(4f)
                        ) { Text(text="Cancel", color = Color.Black) }


                        Spacer(Modifier.weight(2f))


                        OutlinedButton(
                            onClick = {
                                if(!itemQuantityInput.isDigitsOnly()){
                                    Toast.makeText(
                                        context,
                                        "Only numbers are allowed in Item Quantity!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    itemNameInput = ""
                                    itemQuantityInput = ""
                                }
                                else {
                                    if(itemNameInput.isNotBlank()){
                                        if(itemQuantityInput.isNotBlank()) {
                                            val newItem = ShoppingItems(
                                                id = items.size + 1,
                                                name = itemNameInput,
                                                quantity = itemQuantityInput.toInt(),
                                            )
                                            items = items + newItem
                                        }
                                        else{
                                            val newItem = ShoppingItems(
                                                id = items.size + 1,
                                                name = itemNameInput,
                                                quantity = 1,
                                            )
                                            items = items + newItem
                                        }

                                        Toast.makeText(
                                            context,
                                            "Item added!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        itemNameInput = ""
                                        itemQuantityInput = ""
                                        showDialog = false


                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(4f)
                        ) { Text(text="Add", color = Color.Black) }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    item:ShoppingItems,
    onDelete: (ShoppingItems) ->Unit
) {
    var editDialog by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuality by remember { mutableStateOf(item.quantity.toString()) }
    val context = LocalContext.current



    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .border(
                BorderStroke(2.dp, Color.DarkGray), shape = RoundedCornerShape(20)
            ),
        color = Color.Transparent
    ) {
        Row(
            Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically


        ) {

            Text(text = item.name, Modifier.weight(3f))
            Text(text = "Qty: ${item.quantity}", Modifier.weight(3f))
            Row(
                Modifier.weight(3f).fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { editDialog = !editDialog },
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit"
                    )
                }
                Spacer(Modifier.width(16.dp))
                IconButton(
                    onClick = { onDelete(item) }
                ) {
                    Icon(
                        contentDescription = "delete",
                        imageVector = Icons.Default.Delete
                    )
                }
            }
        }
    }

    if (editDialog) {
        Surface {
            BasicAlertDialog(onDismissRequest = { editDialog = !editDialog }) {
                Surface(
                    modifier = Modifier
                        .height(400.dp)
                        .width(300.dp),
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(corner = CornerSize(32.dp))

                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        //verticalArrangement = Arrangement.Center,
                        modifier = Modifier.background(Color(160, 200, 225))
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "Edit Details",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(64.dp))
                        OutlinedTextField(
                            shape = RoundedCornerShape(corner = CornerSize(64.dp)),
                            value = editedName,
                            onValueChange = { editedName = it },
                            placeholder = { Text("Item Name", color = Color.Black) }

                        )
                        Spacer(Modifier.height(24.dp))
                        OutlinedTextField(
                            shape = RoundedCornerShape(corner = CornerSize(64.dp)),
                            value = editedQuality,
                            onValueChange = { editedQuality = it },
                            placeholder = { Text("Item Qantity", color = Color.Black) }

                        )
                        Spacer(Modifier.height(64.dp))
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {

                            OutlinedButton(
                                onClick = { editDialog = false },
                                modifier = Modifier
                                    .weight(4f)
                            ) { Text(text = "Cancel", color = Color.Black) }


                            Spacer(Modifier.weight(2f))


                            OutlinedButton(
                                onClick = {
                                    if (!editedQuality.isDigitsOnly()) {
                                        Toast.makeText(
                                            context,
                                            "Only numbers are allowed in Item Quantity!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        editedName = ""
                                        editedName = ""
                                    } else {
                                        if (editedName.isNotBlank()) {
                                            if (editedQuality.isNotBlank()) {
                                                item.name = editedName
                                                item.quantity = editedQuality.toInt()
                                            } else {
                                                item.name = editedName
                                            }

                                            Toast.makeText(
                                                context,
                                                "Item Edited!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            editedName = ""
                                            editedQuality = ""
                                            editDialog = false


                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(4f)
                            ) { Text(text = "Edit", color = Color.Black) }
                        }
                    }
                }
            }
        }

    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Index()
}