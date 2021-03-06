//authors:
//   The Cult of Mason

package org.team3128.compbot.commands;

import java.lang.invoke.WrongMethodTypeException;
import java.security.GuardedObject;
import java.util.Arrays;

import org.team3128.compbot.subsystems.Constants;
import org.team3128.compbot.subsystems.Thropper;
import org.team3128.compbot.subsystems.Thropper.ThropperState;

import edu.wpi.first.wpilibj.command.Command;

import org.team3128.common.utility.Log;

public class CmdThrintake extends Command {
    Thropper hopper;
    int topCount;
    int middleCount;
    int totalCount;
    boolean empty0 = false;
    boolean empty1 = false;
    boolean empty2 = false;
    boolean broken = false;
    int position = 0;
    boolean[] updatedArray = new boolean[Constants.HopperConstants.CAPACITY];

    boolean inFocus = false;
    int focusPos = 0;

    public CmdThrintake(Thropper hopper) {
        this.hopper = hopper;
    }

    @Override
    protected void initialize() {
        // nothing here
        switch( hopper.getNumBalls()) {
            case 0:
                topCount = 0;
                middleCount = 0;
                totalCount = 0;
                break;
            case 1:
                topCount = 1;
                middleCount = 0;
                totalCount = 1;
                break;
            case 2:
                topCount = 2;
                middleCount = 0;
                totalCount = 2;
                break;
            case 3:
                topCount = 2;
                middleCount = 1;
                totalCount = 3;
                break;
            case 4:
                topCount = 2;
                middleCount = 2;
                totalCount = 4;
                break;
            case 5:
                topCount = 2;
                middleCount = 2;
                totalCount = 5;
                break;
        }
    }

    @Override
    protected void execute() {
        updateCount();
        updatedArray = hopper.getBallArray();
        if (!hopper.isFull()) {
            hopper.INTAKE_MOTOR.set(Constants.IntakeConstants.INTAKE_MOTOR_ON_VALUE);
        } else {
            hopper.INTAKE_MOTOR.set(0);
        }
        switch(middleCount + topCount) {
            case 0:
                updatedArray = new boolean[] {false, false, false, false, false};
                if (totalCount > (middleCount + topCount)) {
                    hopper.setMotorPowers(0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER);
                } else {
                    hopper.setMotorPowers(0, 0, 0, 0);
                }
                break;
            case 1:
                updatedArray = new boolean[] {true, false, false, false, false};
                if (totalCount > (middleCount + topCount)) {
                    hopper.setMotorPowers(0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER);
                    //boolean[] updatedArray = {true, false, false, false, true};
                } else if (topCount < (topCount + middleCount)) {
                    hopper.setMotorPowers(0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER, 0);
                    //boolean[] updatedArray = {true, false, false, false, false};
                } else {
                    hopper.setMotorPowers(0, 0, 0, 0);
                }
                break;
            case 2:
                updatedArray = new boolean[] {true, true, false, false, false};
                if (topCount < 2) {
                    hopper.setMotorPowers(0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER, 0);
                } else if (totalCount > (middleCount + topCount)) {
                    hopper.setMotorPowers(0, 0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER);
                } else {
                    hopper.setMotorPowers(0, 0, 0, 0);
                }
                break;
            case 3:
                updatedArray = new boolean[] {true, true, false, true, false};
                if (topCount < 2) {
                    Log.info("CmdThrintake", "something screwed up; topCount should be 2 but it isn't");
                }
                if (totalCount > (middleCount + topCount)) {
                    hopper.setMotorPowers(0, 0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER);
                } else {
                    hopper.setMotorPowers(0, 0, 0, 0);
                }
                break;
            case 4:
                if (totalCount > (middleCount + topCount)) {
                    updatedArray = new boolean[] {true, true, true, true, true};
                } else{
                    updatedArray = new boolean[] {true, true, true, true, false};
                }
                hopper.setMotorPowers(0, 0, 0, 0);
                break;
        }

        hopper.updateBallArray(updatedArray);
    }

    @Override
    protected boolean isFinished() {
        if (hopper.getNumBalls() == Constants.HopperConstants.CAPACITY) {
            return true;
        }

        return false;
    }

    @Override
    protected void end() {
        // handle what happens when the command is terminated
        hopper.INTAKE_MOTOR.set(0);
        while(!broken) {
            updateCount();
            switch(middleCount + topCount) {
                case 0:
                    updatedArray = new boolean[] {false, false, false, false, false};
                    if (totalCount > (middleCount + topCount)) {
                        hopper.setMotorPowers(0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER);
                    } else {
                        hopper.setMotorPowers(0, 0, 0, 0);
                        broken = true;
                    }
                    break;
                case 1:
                    updatedArray = new boolean[] {true, false, false, false, false};
                    if (totalCount > (middleCount + topCount)) {
                        hopper.setMotorPowers(0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER);
                        //boolean[] updatedArray = {true, false, false, false, true};
                    } else if (topCount < (topCount + middleCount)) {
                        hopper.setMotorPowers(0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER, 0);
                        //boolean[] updatedArray = {true, false, false, false, false};
                    } else {
                        hopper.setMotorPowers(0, 0, 0, 0);
                        broken = true;
                    }
                    break;
                case 2:
                    updatedArray = new boolean[] {true, true, false, false, false};
                    if (topCount < 2) {
                        hopper.setMotorPowers(0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER, 0);
                    } else if (totalCount > (middleCount + topCount)) {
                        hopper.setMotorPowers(0, 0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER);
                    } else {
                        hopper.setMotorPowers(0, 0, 0, 0);
                        broken = true;
                    }
                    break;
                case 3:
                    updatedArray = new boolean[] {true, true, false, true, false};
                    if (topCount < 2) {
                        Log.info("CmdThrintake", "something screwed up; topCount should be 2 but it isn't");
                    }
                    if (totalCount > (middleCount + topCount)) {
                        hopper.setMotorPowers(0, 0, Constants.HopperConstants.BASE_POWER, Constants.HopperConstants.BASE_POWER);
                    } else {
                        hopper.setMotorPowers(0, 0, 0, 0);
                        broken = true;
                    }
                    break;
                case 4:
                    if (totalCount > (middleCount + topCount)) {
                        updatedArray = new boolean[] {true, true, true, true, true};
                    } else{
                        updatedArray = new boolean[] {true, true, true, true, false};
                    }
                    hopper.setMotorPowers(0, 0, 0, 0);
                    broken = true;
                    break;
            }
        }
        hopper.setMotorPowers(0, 0, 0, 0);
    }

    @Override
    protected void interrupted() {
        end(); // check if the balls are in a good position. If not, then shuffle them around
               // to the right position
    }

    private void updateCount() {
        if (!hopper.SENSOR_0.get()) {
            empty0 = true;
        } else if (empty0) {
            totalCount++;
            empty0 = false;
        }
        if (!hopper.SENSOR_1.get()) {
            empty1 = true;
        } else if (empty1) {
            middleCount++;
            totalCount++;
            empty1 = false;
        }
        if (!hopper.SENSOR_2.get()) {
            empty2 = true;
        } else if (empty2) {
            middleCount--;
            topCount++;
            totalCount++;
            empty2 = false;
        }
    }
}