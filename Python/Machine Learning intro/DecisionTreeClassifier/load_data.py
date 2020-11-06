"""
CSC311 Assignment_1 Q2 (a)
"""
from typing import Tuple
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split


def load_data(fake_d: str, real_d: str) -> Tuple:
    """
    Load both fake data and clean data and split them into traing set, validation set, and testing set using the ratio
    of 0.70: 0.15: 0.15.

    :param fake_d: the filename of fake data
    :type fake_d: str
    :param real_d: the filename of clean data
    :type real_d: str
    :return: the splitted training, validation, and training dataset
    :rtype: Tuple(np.ndarray)
    """

    # open and read the fake news file and real news file
    fake_f = open(fake_d, 'r')
    clean_f = open(real_d, 'r')
    fake_content, real_content = fake_f.readlines(), clean_f.readlines()

    # count the number of fake news and real news
    n_fake, n_real = len(fake_content), len(real_content)

    # make the target label vector
    y = [0] * n_fake + [1] * n_real

    content = fake_content + real_content
    vectorizer = CountVectorizer()
    x = vectorizer.fit_transform(content)
    feature_lst = vectorizer.get_feature_names()
    n_data = n_fake + n_real
    n_feature = len(feature_lst)
    x = x.toarray().reshape(n_data, n_feature)

    # split the data set by 7:3 to separate the training set
    x_train, x_remain, y_train, y_remain = train_test_split(x, y, train_size=0.7, random_state=1)

    # split the remaining data set by 1:1 to separate the validation and tetsing sets
    x_val, x_test, y_val, y_test = train_test_split(x_remain, y_remain, test_size=0.5, random_state=1)

    return feature_lst, x_train, y_train, x_val, y_val, x_test, y_test


if __name__ == "__main__":
    features, *data_set = load_data("clean_fake.txt", "clean_real.txt")
