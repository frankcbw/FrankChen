"""
CSC311 Assignment_1 Q3 3.3

data: a variable refers to a (y, X) pair, where y is the target vector and X is the feature matrix
model: a variable refers to the coefficient of the trained model
"""
import numpy as np
import matplotlib.pyplot as plt


def shuffle_data(data):
    """
    Copy and Shuffle the data and return as a new dataset
    """
    return np.random.permutation(data)


def split_data(data, num_folds, fold):
    """
    Evenly partition the data into num_folds sub-data. Return a tuple of two dataset, of which the first one is the
    fold-th partition and the rest data are combined together as the second one.
    """
    d_splitted = np.array_split(data, num_folds)
    d_fold = d_splitted.pop(fold-1)
    d_rest = np.vstack(d_splitted)
    return d_fold, d_rest


def train_model(data, lambd):
    """
    Build a linear regression model with data and the penalty level lambd and return the coefficient of ridge regression.
    """
    y_train = data[:, 0].reshape(-1, 1)
    x_train = data[:, 1:]
    lhs = np.dot(x_train.T, x_train) + lambd * np.identity(x_train.shape[1])
    rhs = np.dot(x_train.T, y_train)

    return np.linalg.solve(lhs, rhs)


def predict(data, model):
    """
    Return the prediction based on data and model.
    """
    return np.dot(data[:, 1:], model)


def loss(data, model):
    """
    Return the average squared error loss based on model.
    """
    y_predict = predict(data, model)
    error = y_predict - data[:, 0].reshape(-1, 1)
    return float((np.dot(error.T, error)/len(y_predict))[0])


def cross_validation(data, num_folds, lambd_seq):
    """
    Cross validate using each lambda in lambd_seq based on data and num_folds.
    """
    cv_error = []
    data = shuffle_data(data)
    for lambd in lambd_seq:
        loss_cv = 0
        for i in range(1, num_folds+1):
            val, train = split_data(data, num_folds, i)
            model = train_model(train, lambd)
            loss_cv += loss(val, model)
        cv_error.append(loss_cv/num_folds)
    return cv_error


def train_test_error(data, test_data, lambd_seq):
    """
    Return a tuple of a list of training errors and a list of testing errors corresponding to each lambd in lambd_seq.
    """
    train_error = []
    test_error = []

    for lambd in lambd_seq:
        model = train_model(data, lambd)
        train_error.append(loss(data, model))
        test_error.append(loss(test_data, model))
    return train_error, test_error


if __name__ == "__main__":

    # import data and combine to create training and testing datasets
    data_train = {'X': np.genfromtxt('data_train_X.csv', delimiter=','),
                  'y': np.genfromtxt('data_train_y.csv', delimiter=',')}
    data_test = {'X': np.genfromtxt('data_test_X.csv', delimiter=','),
                 'y': np.genfromtxt('data_test_y.csv', delimiter=',')}
    data_train = np.hstack((data_train['y'].reshape(-1, 1), data_train['X']))
    data_test = np.hstack((data_test['y'].reshape(-1, 1), data_test['X']))

    # fix a random seed
    np.random.seed(1)

    # generate the lambd sequence
    lambda_seq = np.linspace(0.02, 1.5, num=50)

    # find the error of 5-fold, 10-fold cross validation as well as the training and testing errors
    cv_errors_5 = cross_validation(data_train, 5, lambda_seq)
    cv_errors_10 = cross_validation(data_train, 10, lambda_seq)
    train_error, test_error = train_test_error(data_train, data_test, lambda_seq)
    lambda_5 = np.argmin(cv_errors_5)
    lambda_10 = np.argmin(cv_errors_10)

    # plot errors
    plt.plot(lambda_seq, cv_errors_5, 'b--', label='5-fold')
    plt.plot(lambda_seq, cv_errors_5, ls='', label=r'$\lambda$-5-fold', markevery=[lambda_5], marker='o')
    plt.text(lambda_seq[lambda_5], cv_errors_5[lambda_5]+0.2, f"{lambda_seq[lambda_5]:.3f}, {cv_errors_5[lambda_5]:.3f}")

    plt.plot(lambda_seq, cv_errors_10, 'b-', label='10-fold')
    plt.plot(lambda_seq, cv_errors_10, ls='', label=r'$\lambda$-10-fold', markevery=[lambda_10], marker='o')
    plt.text(lambda_seq[lambda_10]+0.05, cv_errors_10[lambda_10]-0.2, f"{lambda_seq[lambda_10]:.3f}, {cv_errors_10[lambda_10]:.3f}")

    plt.plot(lambda_seq, train_error, 'k--', label='Training error')
    plt.plot(lambda_seq, test_error, 'k-', label='Testing error')

    plt.legend()
    plt.xlabel('Penalty level '+r'$\lambda$')
    plt.ylabel('Errors')
    plt.title(r'$L_2$ Regulator: Errors v.s. $\lambda$')
    plt.show()
