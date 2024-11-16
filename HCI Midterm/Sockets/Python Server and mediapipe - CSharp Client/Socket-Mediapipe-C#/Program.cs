using System;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

class CSocketClient
{
    private TcpClient client;
    private NetworkStream stream;
    private StreamReader reader;

    public async Task CreateConnectionAsync(string host, int port)
    {
        client = new TcpClient();
        await client.ConnectAsync(host, port);
        stream = client.GetStream();
        reader = new StreamReader(stream);
        Console.WriteLine("Connected");
    }

    public void CloseConnection()
    {
        if (reader != null)
            reader.Close();
        if (stream != null)
            stream.Close();
        if (client != null)
            client.Close();
    }

    public async Task ReceiveAndPrintJsonAsync()
    {
        try
        {
            while (true)
            {
                if (Console.KeyAvailable)
                {
                    var key = Console.ReadKey(intercept: true).Key;
                    if (key == ConsoleKey.E)
                        break;
                }

                string data = await reader.ReadLineAsync();

                if (string.IsNullOrEmpty(data))
                    break;

                // Deserialize JSON data to a dictionary
                var wristCoords = JsonSerializer.Deserialize<System.Collections.Generic.Dictionary<string, float>>(data);
                Console.WriteLine("Received Wrist Coordinates:");
                foreach (var kvp in wristCoords)
                {
                    Console.WriteLine($"{kvp.Key}: {kvp.Value}");
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine("Error: " + e.Message);
        }
        finally
        {
            CloseConnection(); // Close the connection when exiting the loop
        }
    }
}

class Program
{
    static async Task Main()
    {
        CSocketClient socketClient = new CSocketClient();

        try
        {
            await socketClient.CreateConnectionAsync("localhost", 12345);
            await socketClient.ReceiveAndPrintJsonAsync();
        }
        finally
        {
            socketClient.CloseConnection();
        }
    }
}