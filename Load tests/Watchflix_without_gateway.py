from locust import HttpUser, task, between
import time
import random
import gevent

class WebsiteUser(HttpUser):
    wait_time = between(10,15) 
    movieId = "4b318b11-8af8-47bd-b523-21f632f6acfd"
    movieName = "The Brutalist"
    wrongMovieId = "1-afdj-1"
    userId="6df588f1-854d-4939-b800-bc6eb5cc450c"
    username="john.doe"
    timeout=1
    did_get_movie_file=False

    def on_start(self):
        # Add a token to each request
        self.headers = {
            "X-user-id": self.userId
        }
        # So the get movie file hits only once
        self.start_time = time.time()
        delay = random.randint(1, 1000)
        gevent.spawn_later(delay, self.getMovieFile)
            
    @task
    def getMovie(self):
        with self.client.get(f"http://131.189.139.126:9090/movie/{self.movieId}", headers=self.headers, timeout=self.timeout, catch_response=True) as response:
            elapsed = response.elapsed.total_seconds()
            if elapsed > self.timeout:
                response.failure(f"Request took too long: {elapsed:.2f}s")
            elif self.movieName not in response.text:
                response.failure(f"This returned {response.text}, but {self.movieName} was expected")
            else:
                response.success()

# SHOULD BE 400? 
    @task
    def getMovieThatDoesNotExist(self):
        with self.client.get(f"http://131.189.139.126:9090/movie/{self.wrongMovieId}", headers=self.headers, catch_response=True) as response:       
            elapsed = response.elapsed.total_seconds()
            if elapsed > self.timeout:
                    response.failure(f"Request took too long: {elapsed:.2f}s")     
            elif response.status_code == 500:
                elapsed = response.elapsed.total_seconds()
                response.success()
            else:
                response.failure(f"A 400 bad request was expected, but status_code {response.status_code} was returned")

    @task
    def getMovies(self):
        with self.client.get("http://131.189.139.126:9090/movie/nr/10", headers=self.headers, timeout=self.timeout, catch_response=True) as response:
            elapsed = response.elapsed.total_seconds()
            if elapsed > self.timeout:
                response.failure(f"Request took too long: {elapsed:.2f}s")
            else:
                response.success()

    def getMovieFile(self):
        if not self.did_get_movie_file:
            self.did_get_movie_file = True
            with self.client.get(f"http://131.189.238.186:9091/movieFile/{self.movieId}", headers=self.headers, timeout=self.timeout, catch_response=True) as response:
                elapsed = response.elapsed.total_seconds()
                if elapsed > self.timeout:
                    response.failure(f"Request took too long: {elapsed:.2f}s")
                else:
                    content_type = response.headers.get('Content-Type', '')
                    if 'video' not in content_type:
                        response.failure(f"Expected video, got {content_type}")
                    else:
                        response.success()