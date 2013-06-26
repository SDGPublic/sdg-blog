using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Phone.Controls;

namespace GoogleOAuth2._0
{
    public partial class MainPage : PhoneApplicationPage
    {
        public const string CLIENTID = "MUST FILL IN Your Google client ID";

        public const string CLIENTSECRET = "MUST FILL IN Your Google client secret code";

        // Constructor
        public MainPage()
        {
            InitializeComponent();
            this.AuthBrowser.Navigating += new EventHandler<NavigatingEventArgs>(AuthBrowser_Navigating);
            this.AuthBrowser.Navigate(
                new Uri(
                    string.Format("https://accounts.google.com/o/oauth2/auth?response_type=code&client_id={0}&scope=https://www.googleapis.com/auth/userinfo.email&redirect_uri=http://localhost&approval_prompt=force", CLIENTID)));
        }

        private async void AuthBrowser_Navigating(object sender, NavigatingEventArgs e)
        {
            if (string.IsNullOrEmpty(e.Uri.Query))
            {
                return;
            }
            string query = HttpUtility.UrlDecode(e.Uri.Query);

            var dic = query.TrimStart('?').Split('&').Select(p => new KeyValuePair<string, string>(p.Split('=').FirstOrDefault(), p.Split('=').LastOrDefault()));

            if (dic.Any(q => q.Key == "code"))
            {
                this.AuthBrowser.Visibility = System.Windows.Visibility.Collapsed;

                // get the code from the query string
                var code = dic.FirstOrDefault(k => k.Key == "code");

                // query the google authentication server to get the access token
                HttpWebRequest request = (HttpWebRequest)WebRequest.CreateHttp("https://accounts.google.com/o/oauth2/token");
                var token = await request.GetValueFromRequest<GoogleToken>(string.Format(
                    "code={0}&client_id={1}&client_secret={2}&redirect_uri={3}&grant_type={4}",
                    code.Value,
                    CLIENTID,
                    CLIENTSECRET,
                    "http://localhost",
                    "authorization_code"));

                // now use the code to get the users email
                request = (HttpWebRequest)WebRequest.CreateHttp("https://www.googleapis.com/oauth2/v2/userinfo");
                request.Headers["Authorization"] = string.Format("{0} {1}", token.TokenType, token.AccessToken);
                var userEmail = await request.GetValueFromRequest<UserEmail>();

                this.UserEmail.Text = userEmail.Email;

                this.UserEmail.Visibility = System.Windows.Visibility.Visible;
            }
        }
    }

    [DataContract]
    public class GoogleToken
    {
        // JSON
        //{
        //  "access_token" : "value",
        //  "token_type" : "Bearer",
        //  "expires_in" : 3600,
        //  "id_token" : "value",
        //  "refresh_token" : "value"
        //}
        [DataMember(Name = "access_token")]
        public string AccessToken { get; set; }
        [DataMember(Name = "token_type")]
        public string TokenType { get; set; }
    }

    [DataContract]
    public class UserEmail
    {
        // JSON
        //{
        // "id": "id returned from service",
        // "email": "users email",
        // "verified_email": true
        //}
        [DataMember(Name = "email")]
        public string Email { get; set; }
    }

    public static class HttpExtensions
    {
        private static Task<Stream> GetRequestStreamAsync(this WebRequest request)
        {
            return Task.Factory.FromAsync<Stream>(request.BeginGetRequestStream, request.EndGetRequestStream, null);
        }

        // http://blogs.msdn.com/b/andy_wigley/archive/2013/02/07/async-and-await-for-http-networking-on-windows-phone.aspx
        public static Task<HttpWebResponse> GetResponseAsync(this HttpWebRequest request)
        {
            var taskComplete = new TaskCompletionSource<HttpWebResponse>();

            request.BeginGetResponse(asyncResponse =>
            {
                try
                {
                    HttpWebRequest responseRequest = (HttpWebRequest)asyncResponse.AsyncState;
                    HttpWebResponse someResponse = (HttpWebResponse)responseRequest.EndGetResponse(asyncResponse);
                    taskComplete.TrySetResult(someResponse);
                }
                catch (WebException webExc)
                {
                    HttpWebResponse failedResponse = (HttpWebResponse)webExc.Response;
                    taskComplete.TrySetResult(failedResponse);
                }
            }, request);

            return taskComplete.Task;
        }

        public async static Task<T> GetValueFromRequest<T>(this HttpWebRequest request, string postData = null)
        {
            T returnValue = default(T);

            if (!string.IsNullOrEmpty(postData))
            {
                byte[] requestBytes = Encoding.UTF8.GetBytes(postData);

                request.Method = "POST";
                request.ContentType = "application/x-www-form-urlencoded";
                request.ContentLength = requestBytes.Length;

                using (var postStream = await request.GetRequestStreamAsync())
                {
                    await postStream.WriteAsync(requestBytes, 0, requestBytes.Length);
                }
            }
            else
            {
                request.Method = "GET";
            }

            var response = await request.GetResponseAsync();

            if (response != null)
            {
                using (var receiveStream = response.GetResponseStream())
                {
                    using (var reader = new StreamReader(receiveStream))
                    {
                        var json = await reader.ReadToEndAsync();

                        var serializer = new DataContractJsonSerializer(typeof(T));

                        using (var tempStream = new MemoryStream(Encoding.UTF8.GetBytes(json)))
                        {
                            return (T)serializer.ReadObject(tempStream);
                        }
                    }
                }
            }

            return returnValue;
        }
    }
}