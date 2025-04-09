from locust import HttpUser, task, between, events

class WebsiteUser(HttpUser):
    # A client waits between n1 and n2 time before making a new request
    wait_time = between(1, 5) 
    movieId = 2
    movieName = "Challengers"
    wrongMovieId = 20

    @task
    def getMovie(self):
        with self.client.get(f"/movie/{self.movieId}", catch_response=True) as response:
            if self.movieName not in response.text:
                response.failure(f"This returned {response.text}, but {self.movieName} was expected")

    @task
    def getMovieThatDoesNotExist(self):
        with self.client.get(f"/movie/{self.wrongMovieId}", catch_response=True) as response:            
            if response.status_code == 400:
                response.success()
            else:
                response.failure(f"A 400 bad request was expected, but status_code {response.status_code} was returned")

    @task
    def getMovies(self):
        self.client.get("/movie/10")

    @task
    def getMovieFile(self):
        with self.client.get(f"/movieFile/{self.movieId}", catch_response=True) as response:
            if 'video' not in response.headers['Content-Type']:
                response.failure(f"This returned a {response.headers['Content-Type']}, but a video was expected")
            elif "Challengers" not in response.headers["Content-Disposition"]:
                response.failure(f"This returned {response.headers["Content-Disposition"]}, but {self.movieName} was expected in the filename")
