#! /usr/bin/env python3

import json
import urllib.request

TEST_VECTORS_URL = "https://github.com/BLAKE3-team/BLAKE3/raw/master/test_vectors/test_vectors.json"


def main():
    contents = urllib.request.urlopen(TEST_VECTORS_URL).read()
    test_vectors = json.loads(contents)
    for case in test_vectors["cases"]:
        print(f"""
    "{case["input_len"]}" in {{
      runTestCase(
        inputLen = {case["input_len"]},
        hash = "{case["hash"]}",
        keyedHash = "{case["keyed_hash"]}",
        deriveKeyHash = "{case["derive_key"]}"
      )
    }}
""")



if __name__ == "__main__":
    main()
