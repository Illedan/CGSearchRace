using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.IO;
using System.Linq;
using Newtonsoft.Json;

namespace SR.MapGenerator
{
    class Program
    {
        private const int MinDist = 1500, Width = 16000, Height = 9000, MinPos = 1000;
        private static Position Center = new Position(Width / 2, Height / 2);
        private static Random rnd = new Random(42);
        static void Main()
        {
            for (var i = 1; i < 151; i++)
            {
                var test = Generate(i < 20, i < 15 ? rnd.Next(2, 8) : 8, "test" + i);
                WriteTest("test" + i + ".json", test);
            }
        }

        static void WriteTest(string name, TestCase testCase)
        {
            File.WriteAllText(Path.Combine("../../../../../config/", name), JsonConvert.SerializeObject(testCase));
        }

        static TestCase Generate(bool isTest, int checkpoints, string name)
        {
            var points = new List<Position>();
            for(var i = 0; i < checkpoints; i++)
            {
                var next = GenerateRndPosition();
                while(points.Any(p => p.Dist(next) < MinDist))
                {
                    next = GenerateRndPosition();
                }
                points.Add(next);
            }

            return new TestCase
            {
                isTest = isTest.ToString().ToLower(),
                isValidator = "true",
                title = new Title { num1 = name, num2 = name },
                testIn = MergePoints(points),
            };
        }

        static string MergePoints(List<Position> points) => string.Join(";", points.Select(p => p.X + " " + p.Y));

        static Position GenerateRndPosition() => new Position(rnd.Next(MinPos, Width- MinPos), rnd.Next(MinPos, Height- MinPos));
    }

    public class Position
    {
        public int X, Y;
        public Position(int x, int y)
        {
            X = x;
            Y = y;
        }

        public double Dist(Position p2) => Math.Sqrt(Math.Pow(X - p2.X, 2) + Math.Pow(Y - p2.Y, 2));
    }


    public class TestCase
    {
        [JsonProperty("title")]
        public Title title { get; set; }

        [JsonProperty("testIn")]
        public string testIn { get; set; }

        [JsonProperty("isTest")]
        public string isTest { get; set; }

        [JsonProperty("isValidator")]
        public string isValidator { get; set; }
    }

    public class Title
    {
        [JsonProperty("2")]
        public string num2 { get; set; }

        [JsonProperty("1")]
        public string num1 { get; set; }
    }
}
